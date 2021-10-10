package com.meesho.smssystem.controller;


import com.meesho.smssystem.dtos.requests.blacklistRequest.BlacklistBulkSave;
import com.meesho.smssystem.dtos.response.blacklistresponse.GetBlacklistedNumbersResponse;
import com.meesho.smssystem.dtos.response.blacklistresponse.SuccessfulMessageAsResponse;
import com.meesho.smssystem.dtos.response.error.ErrorResponse;
import com.meesho.smssystem.service.authentication.AuthenticationService;
import com.meesho.smssystem.service.blacklist.IBlacklistService;
import javassist.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import javax.validation.Valid;

import static com.meesho.smssystem.constants.Constants.authenticationErrorMessage;


@RestController
@RequestMapping("/v1/blacklist")
public class BlacklistController {

    private final IBlacklistService blacklistService;
    private final AuthenticationService authenticationService;
    private final String authenticationFieldName = "${server.authentication.field.name}";

    private final String duplicateEntryErrorMessage = "Entry already exists";
    private final String successfulBlacklistMessage = "All Entries Successfully blacklisted";
    private final String deletionSuccessfulMessage = "Phone Number removed from the blacklist";

    private static final Logger logger = LogManager.getLogger(BlacklistController.class);

    @Autowired
    public BlacklistController(IBlacklistService blacklistService, AuthenticationService authenticationService) {
        this.blacklistService = blacklistService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("")
    public ResponseEntity<?> getAllListOfNumbers(@RequestHeader(required = false, value = authenticationFieldName ) String authKey) {
        final GetBlacklistedNumbersResponse getBlacklistedNumbersResponse;
        try {
            authenticationService.authenticator(authKey);
            getBlacklistedNumbersResponse = blacklistService.getAllList();
            return new ResponseEntity<>(getBlacklistedNumbersResponse, HttpStatus.OK);
        }
        catch (AuthenticationException | NullPointerException  exception){
            // NullPointerException is thrown by String.equals() method if String is Null
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .error(authenticationErrorMessage)
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
        catch (Exception exception) {
            logger.error("Fetching Blacklist numbers failed!", exception);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("")
    public ResponseEntity<?> saveAllListOfNumbers(@Valid @RequestBody BlacklistBulkSave blacklistBulkSave
            , @RequestHeader(required = false, value =  authenticationFieldName) String authKey ) {
        try {
            authenticationService.authenticator(authKey);
            blacklistService.InsertAllNumbers(blacklistBulkSave);
            SuccessfulMessageAsResponse successfulMessageAsResponse = SuccessfulMessageAsResponse.builder()
                    .data(successfulBlacklistMessage)
                    .build();
            return new ResponseEntity<>(successfulMessageAsResponse, HttpStatus.CREATED);
        }
        catch (AuthenticationException | NullPointerException  exception){
            // NullPointerException is thrown by String.equals() method if String is Null
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .error(authenticationErrorMessage)
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
        // Null List Exception
        catch (NotFoundException exception){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch(DataIntegrityViolationException exception){
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .error(duplicateEntryErrorMessage)
                    .build();
            return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
        }
        catch (Exception exception) {
            logger.error("Bulk saving blacklist request failed!", exception);
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("/{phoneNumber}")
    public ResponseEntity<?> deleteByPhoneNumber(@PathVariable String phoneNumber
            , @RequestHeader(required = false, value =  authenticationFieldName) String authKey) {
        try {
            authenticationService.authenticator(authKey);
            blacklistService.deleteByPhoneNumber(phoneNumber);
            SuccessfulMessageAsResponse deletionSuccessfulResponse = SuccessfulMessageAsResponse.builder()
                    .data(deletionSuccessfulMessage)
                    .build();
            return new ResponseEntity<>(deletionSuccessfulResponse, HttpStatus.OK);
        }
        catch (AuthenticationException | NullPointerException  exception){
            // NullPointerException is thrown by String.equals() method if String is Null
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .error(authenticationErrorMessage)
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
        catch (Exception exception) {
            logger.error("Deletion of entry: " + phoneNumber + " failed!",exception);
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}

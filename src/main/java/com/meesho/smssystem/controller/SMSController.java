package com.meesho.smssystem.controller;

import com.meesho.smssystem.dtos.requests.smsRequests.GetSMSByIdRequest;
import com.meesho.smssystem.dtos.requests.smsRequests.SMSByPhoneNumberBetweenDatesRequest;
import com.meesho.smssystem.dtos.requests.smsRequests.SMSByTextRequest;
import com.meesho.smssystem.dtos.requests.smsRequests.SendSMSRequest;
import com.meesho.smssystem.dtos.response.error.ErrorResponse;
import com.meesho.smssystem.dtos.response.smsresponse.SendSMSResponse;
import com.meesho.smssystem.dtos.response.smsresponse.getsmsbyidresponse.GetSMSByIdResponse;
import com.meesho.smssystem.service.authentication.AuthenticationService;
import com.meesho.smssystem.service.messaging.ISMSRequestService;
import javassist.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.client.ResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import javax.validation.Valid;

@RestController
@RequestMapping("/v1/sms")
public class SMSController {
    private final ISMSRequestService ismsRequestService;
    private final AuthenticationService authenticationService;

    private final String authenticationFieldName = "${server.authentication.field.name}";
    private final String authenticationErrorMessage = "Token Invalid or Not Present in the Header";

    private static final Logger logger = LogManager.getLogger(SMSController.class);

    @Autowired
    public SMSController(ISMSRequestService ismsRequestService, AuthenticationService authenticationService) {
        this.ismsRequestService = ismsRequestService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("")
    ResponseEntity<?> getSMSById (@Valid @RequestBody GetSMSByIdRequest getSmsByIdRequest
            , @RequestHeader(required = false, value = authenticationFieldName) String authKey){
        final GetSMSByIdResponse getSmsByIdResponse;
        try {
            authenticationService.authenticator(authKey);
            getSmsByIdResponse = ismsRequestService.getSMSById(getSmsByIdRequest.getRequest_id());
            return new ResponseEntity<>(getSmsByIdResponse,HttpStatus.OK);
        }
        catch (AuthenticationException | NullPointerException  exception){
            // NullPointerException is thrown by String.equals() method if String is Null
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .error(authenticationErrorMessage)
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
        catch (NumberFormatException exception){
            logger.error(exception);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        catch (NotFoundException exception){
            return new ResponseEntity<>(exception.getMessage(),HttpStatus.NOT_FOUND);
        }
        catch (Exception exception){
            logger.error(exception);
            return new ResponseEntity<>(exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/send")
    ResponseEntity<?> sendSMS (@Valid @RequestBody SendSMSRequest sendSMSRequest
            , @RequestHeader(required = false, value = authenticationFieldName) String authKey) {
        final SendSMSResponse sendSMSResponse;
        try {
            authenticationService.authenticator(authKey);
            sendSMSResponse = ismsRequestService.sendSMSProcess(sendSMSRequest);
            return new ResponseEntity<>(sendSMSResponse,sendSMSResponse.getHttpStatus());
        }
        catch (AuthenticationException | NullPointerException  exception){
            // NullPointerException is thrown by String.equals() method if String is Null
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .error(authenticationErrorMessage)
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
        catch (InvalidDataAccessApiUsageException exception){
            return new ResponseEntity<>(exception.getCause().getMessage(),HttpStatus.BAD_REQUEST);
        }
        catch (Exception exception){
            logger.error(exception);
            return new ResponseEntity<>(exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    @PostMapping("/get-by-text")
    ResponseEntity<?> getSmsByText(@Valid @RequestBody SMSByTextRequest smsByTextRequest
            , @RequestHeader(required = false, value = authenticationFieldName) String authKey){
        final String responsePage;
        try{
            authenticationService.authenticator(authKey);
            responsePage = ismsRequestService.getSMSbyText(smsByTextRequest);
            return new ResponseEntity<>(responsePage,HttpStatus.OK);
        }
        catch (AuthenticationException | NullPointerException  exception){
            // NullPointerException is thrown by String.equals() method if String is Null
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .error(authenticationErrorMessage)
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
        catch (ResponseException exception){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        catch (Exception exception){
            logger.error(exception);
            return new ResponseEntity<>(exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @PostMapping("/get-between")
    ResponseEntity<?> getSmsByPhoneNumberBetweenDates(@Valid @RequestBody SMSByPhoneNumberBetweenDatesRequest smsByPhoneNumberBetweenDatesRequest
            , @RequestHeader(required = false, value = authenticationFieldName) String authKey){
        final String responsePage;
        try{
            authenticationService.authenticator(authKey);
            responsePage = ismsRequestService.getSMSbyPhoneNumberBetweenDates(smsByPhoneNumberBetweenDatesRequest);
            return new ResponseEntity<>(responsePage,HttpStatus.OK);
        }
        catch (AuthenticationException | NullPointerException  exception){
            // NullPointerException is thrown by String.equals() method if String is Null
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .error(authenticationErrorMessage)
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
        catch (Exception exception){
            logger.error(exception);
            return new ResponseEntity<>(exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }


}

package com.meesho.smssystem.dtos.requests.blacklistRequest;

import com.meesho.smssystem.constants.Constants;
import lombok.Data;

import javax.validation.constraints.Pattern;
import java.util.List;

@Data

public class BlacklistBulkSave {
    List<@Pattern(regexp = Constants.regexpForPhoneNumber) String>  phone_numbers;
}

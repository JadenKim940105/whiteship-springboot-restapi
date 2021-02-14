package me.summerbell.whiteshipspringbootrestapi.events;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;

@Component
public class EventValidator {

    public void validate(EventDto eventDto, Errors errors){
        if(eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() > 0){
            errors.rejectValue("basePrice", "wrongValue", "BasePrice is wrong");
            errors.rejectValue("maxPrice", "wrongValue", "maxPrice is wrong");
            errors.reject("wrongPrices", "Values for prices are wrong");
        }
        LocalDateTime closeEnrollmentDateTime = eventDto.getCloseEnrollmentDateTime();
        if(closeEnrollmentDateTime.isBefore(eventDto.getBeginEnrollmentDateTime())){
            errors.rejectValue("closeEnrollmentDateTime", "wrongValue", "closeEnrollmentDateTime is wrong");
        }
    }
}

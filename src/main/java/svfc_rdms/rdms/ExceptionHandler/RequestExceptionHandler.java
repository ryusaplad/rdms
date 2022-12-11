package svfc_rdms.rdms.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RequestExceptionHandler {

     @ExceptionHandler(value = { ApiRequestException.class })
     public ResponseEntity<Object> handleApiException(ApiRequestException e) {
          HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
          Exception customeException = new Exception(e.getMessage(), e,
                    httpStatus,
                    ZonedDateTime.now(ZoneId.of("Z")));

          return new ResponseEntity<>(customeException.getMessage(), httpStatus);
     }

}

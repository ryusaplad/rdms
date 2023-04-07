package svfc_rdms.rdms.ExceptionHandler;

import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Exception {

     private final String message;
     private final Throwable Throwable;
     private final HttpStatus httpStatus;
     private final ZonedDateTime timestamp;

}

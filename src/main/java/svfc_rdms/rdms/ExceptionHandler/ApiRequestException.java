package svfc_rdms.rdms.ExceptionHandler;

public class ApiRequestException extends RuntimeException {

     public ApiRequestException(String message) {
          super(message);
     }

     public ApiRequestException(String message, Throwable cause) {
          super(message, cause);
     }

}

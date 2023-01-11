package svfc_rdms.rdms.ExceptionHandler;

public class UserNotFoundException extends RuntimeException {

     public UserNotFoundException(String message) {
          super(message);
     }

     public UserNotFoundException(String message, Throwable cause) {
          super(message, cause);
     }

}

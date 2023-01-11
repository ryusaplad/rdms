package svfc_rdms.rdms.ExceptionHandler;

public class FileNotFoundException extends RuntimeException {

     public FileNotFoundException(String message) {
          super(message);
     }

     public FileNotFoundException(String message, Throwable cause) {
          super(message, cause);
     }

}

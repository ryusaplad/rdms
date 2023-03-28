package svfc_rdms.rdms.controller.Controllers;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class CustomErrorController implements ErrorController {

    @RequestMapping
    public String handleError(HttpServletRequest request, Model model) {
        int statusCode = getStatusCode(request);
        String errorMessage = getErrorMessage(request);
        String errorTitle = getErrorTitle(statusCode);

        model.addAttribute("errorNumber", statusCode);
        model.addAttribute("errorTitle", errorTitle);
        model.addAttribute("errorMessage", errorMessage);

        return "/error/custom-error";
    }

    private int getStatusCode(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        return statusCode != null ? statusCode : HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    private String getErrorMessage(HttpServletRequest request) {
        return (String) request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
    }

    private String getErrorTitle(int statusCode) {
        if (statusCode == HttpStatus.NOT_FOUND.value()) {
            return "PAGE NOT FOUND !";
        } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            return "INTERNAL SERVER ERROR !";
        } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
            return "FORBIDDEN !";
        } else {
            return "PAGE NOT FOUND !";
        }
    }

}

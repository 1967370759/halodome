package run.halo.app.security.handler;

import run.halo.app.exception.HaloException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface AuthenticationFailureHandler {

    void onFailure(HttpServletRequest request, HttpServletResponse response, HaloException exception) throws IOException , ServletException;


}

package hello.servlet.web.frontcontroller.v3;

import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.MyView;

import hello.servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@WebServlet(name = "frontControllerServletV3", urlPatterns = "/front-controller/v3/*")
public class FrontControllerServletV3 extends HttpServlet {

    private Map<String, ControllerV3> controllerMap = new HashMap<>();

    public FrontControllerServletV3() {
        System.out.println("FrontControllerServletV3호출");
        controllerMap.put("/front-controller/v3/members/new-form", new MemberFormControllerV3());
        System.out.println("1");
        controllerMap.put("/front-controller/v3/members/save", new MemberSaveControllerV3());
        System.out.println("2");
        controllerMap.put("/front-controller/v3/members", new MemberListControllerV3());
        System.out.println("3");
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("request = " + request);
        String requestURI = request.getRequestURI();
        System.out.println("requestURI = " + requestURI);
        // ControllerV1 controller = new MemberListController();
        // 부모는 자식을 받을 수 있음
        ControllerV3 controller = controllerMap.get(requestURI);
        System.out.println("controller = " + controller);
        if (controller == null){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Map<String, String> paramMap = createParamMap(request);
        System.out.println("paramMap = " + paramMap);
        // override 된 메소드 부터 먼저 호출 없으면 부모 호출
        ModelView mv = controller.process(paramMap);
        System.out.println("mv = " + mv);

        // new-form
        String viewName = mv.getViewName();
        System.out.println("viewName = " + viewName);
        MyView view = viewResolver(viewName);
        System.out.println("view = " + view);


        //model에 담긴 정보가 뭐지? 필요한 모델 정보가 뭐야??
        System.out.println("mv.getModel이 뭐야" + mv.getModel());
        view.render(mv.getModel(), request,response);
    }

    private MyView viewResolver(String viewName) {
        System.out.println("viewResolver 호출");
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }

    private Map<String, String> createParamMap(HttpServletRequest request) {
        System.out.println("createParamMap 호출");
        Map<String, String> paramMap = new HashMap<>();
        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> paramMap.put(paramName, request.getParameter(paramName)));
        return paramMap;
    }
}

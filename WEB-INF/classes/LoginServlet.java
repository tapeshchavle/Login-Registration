import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginServlet extends HttpServlet{
    private Connection conn;
    private PreparedStatement ps;
    public void init()throws ServletException{
        try{
            conn=DriverManager.getConnection("jdbc:oracle:thin:@//LAPTOP-S1QPSLM6:1521/xe","tapesh","abc");
            System.out.println("connected succesfully");
            ps=conn.prepareStatement("select name from users where userid=? and password=?");
        }
        catch(SQLException sq){
            System.out.println("some problem in init"+sq);
            ServletException obj=new ServletException(sq.getMessage());
            throw obj;
        }
    }
    protected void doPost(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
        resp.setContentType("text/html");
        PrintWriter pw=resp.getWriter();
        pw.println("<html><head><title>Login Page</title></head>");
        pw.println("<body>");
        String userid=req.getParameter("userid");
        String pwd=req.getParameter("password");
        try{
            ps.setString(1,userid);
            ps.setString(2,pwd);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                String username=rs.getString(1);
                pw.println("<h2>Welcome "+username+"</h2>");
                pw.println("<p>Login successful.Enjoy surfing!</p>");
            }
            else{
                pw.println("<h2>Sorry!</h2>");
                pw.println("<p>Login rejected.Invalid userid/password!</p>");
                pw.println("<p>Try again <a href='login.html'>Login Again</a></p>");
                pw.println("<a href='registration.html'>New User?</a>");
            }
        }
        catch(SQLException sq){
            System.out.println("Some Problem in dopost"+sq);
            pw.println("<h2>Sorry!</h2>");
            pw.println(("<p>Server is experiencing some issues.Try later</p>"));
        }
        pw.println("</body></html>");
        pw.close();
    
    }
    public void destroy(){
        try{
            conn.close();
        }
        catch(SQLException sq){
            System.out.println("some problem during closing the connection");
        }
    }
}
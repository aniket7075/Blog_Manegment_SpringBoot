package com.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.dao.UserDao;
import com.entity.Message;
import com.entity.User;
import com.helper.ConnectionProvider;
import com.helper.MyUtility;

@WebServlet("/EditServlet")
@MultipartConfig
public class EditServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public EditServlet() {
        super();
    }

    // doGet method can be left empty or you can provide some info if necessary
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().append("Served at: ").append(request.getContextPath());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentuser");
        if (currentUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int id = 0;
        String name = "";
        String email = "";
        String password = "";
        String gender = "";
        String about = "";
        String profile = "";
        
      
        String path = getServletContext().getRealPath("/") + "image/";

        try {
            // Use Apache Commons FileUpload to handle file uploads
            List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);

            for (FileItem item : items) {
                if (item.isFormField()) {
                    // Handle form fields
                    if (item.getFieldName().equals("id")) {
                        id = Integer.parseInt(item.getString().trim());
                    } else if (item.getFieldName().equals("name")) {
                        name = item.getString().trim();
                    } else if (item.getFieldName().equals("email")) {
                        email = item.getString().trim();
                    } else if (item.getFieldName().equals("password")) {
                        password = item.getString().trim();
                    } else if (item.getFieldName().equals("gender")) {
                        gender = item.getString().trim();
                    } else if (item.getFieldName().equals("about")) {
                        about = item.getString().trim();
                    }
                } else {
                  
                    if (item.getSize() > 0) {
                        MyUtility myUtility = new MyUtility();
                        profile = myUtility.uploadFile(name, item, path, null, currentUser.getProfile());
                    }
                }
            }

           
            User updatedUser = new User();
            updatedUser.setId(id);
            updatedUser.setName(name);
            updatedUser.setEmail(email);
            updatedUser.setPassword(password);
            updatedUser.setGender(gender);
            updatedUser.setAbout(about);
            updatedUser.setProfile(profile);

            
            UserDao userDao = new UserDao(ConnectionProvider.getConnection());
            int result = userDao.updateUser(updatedUser, id);

            if (result > 0) {
               
                User currentUserUpdated = userDao.getUserByEmailAndPassword(email, password);
                session.setAttribute("currentuser", currentUserUpdated);

               
                Message msg = new Message("User updated successfully", "success", "alert-success");
                session.setAttribute("msg", msg);
                response.sendRedirect("profile.jsp");
            } else {
              
                Message msg = new Message("Failed to update user", "error", "alert-danger");
                session.setAttribute("msg", msg);
                response.sendRedirect("profile.jsp");
            }
        } catch (FileUploadException e) {
            e.printStackTrace();
          
            Message msg = new Message("File upload failed: " + e.getMessage(), "error", "alert-danger");
            session.setAttribute("msg", msg);
            response.sendRedirect("profile.jsp");
        } catch (Exception e) {
            e.printStackTrace();
           
            Message msg = new Message("An error occurred while updating user.", "error", "alert-danger");
            session.setAttribute("msg", msg);
            response.sendRedirect("profile.jsp");
        }
    }
}

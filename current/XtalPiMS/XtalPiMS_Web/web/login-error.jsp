<!DOCTYPE html
PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"> 
    
    <%@page session = "true" %>
    <%@page contentType  = "text/html"%>
    <%@page pageEncoding = "UTF-8"%>
    
    <head>
        <meta http-equiv="CACHE-CONTROL" content="NO-CACHE"/>
        <meta http-equiv="PRAGMA" content="NO-CACHE"/>
        <meta http-equiv="expires" content="0" />
        
        <title>[PiMS]: [Xtal]: Login</title>
        <link rel="stylesheet" href="${pageContext.request['contextPath']}/css/layout.css" type="text/css" />
        <link rel="stylesheet" href="${pageContext.request['contextPath']}/css/header.css" type="text/css" />
        <meta http-equiv="Refresh" content="3; URL=${pageContext.request['contextPath']}/loginhome.jsp" />

    </head>
    <body>
        <script type="text/javascript">
        window.onload = function() {
            document.getElementById( 'username' ).focus();
        } 
        </script>
        
        <div align='center'>
            <form method="post" action="j_security_check">
                <table  >
                    <tr style="height: 40"><td>&nbsp;</td></tr>
                    <tr style="height: 20"><th colspan='3'>&nbsp;</th></tr>
                        <tr>
                            <td style="text-align: center">
                                
                                <img src = "./images/logo.gif" alt="PiMS" width="250" style="border: 0; valign: absmiddle;" />
                            </td>
                        </tr>
                        <tr>
                        <td>
                            <p style="text-align:center;">Unfortunately there has been a problem with your login, please try again!</p>
                            <p style="text-align:center;">You will be redirected back to the login screen in a few seconds</p>

                        </td>
                    </tr>
                    <tr style="height: 20"><th colspan="3">&nbsp;</th></tr>
                </table>
            </form>
        </div>
    </body>
</html>

        

    
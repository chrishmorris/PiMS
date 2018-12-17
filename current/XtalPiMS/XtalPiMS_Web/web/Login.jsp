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
        
        <link rel="stylesheet" href="${pageContext.request['contextPath']}/css/list.css" type="text/css" />
    </head>
    <body>
        <script type="text/javascript">
        window.onload = function() {
            document.getElementById( 'username' ).focus();
        } 
        </script>
        
        <div align='center'>
            
            <form method="post" action="j_security_check">
                <table>
                    <tr style="height: 40"><td>&nbsp;</td></tr>
                    <tr style="height: 20"><td colspan='3'>&nbsp;</td></tr>
                        <tr>
                            <td style="text-align: center">
                                
                                <img src='${pageContext.request['contextPath']}/skins/academic/footer_logo.png' alt='xtalPiMS' style='border: 0; valign: absmiddle;' />
                            </td>
                        </tr>
                        <tr>
                        <td>
                            <p>Please enter your username and password to log on</p>
                            <div align='center'>
                                <table class="list" style="align:center;" border="0" cellpadding="0" cellspacing="0" >		
                                    <tr><th style="text-align: left">Username:</th><td style="text-align: left"><input id="username" type="text" name="j_username"/></td></tr>
                                    <tr><th style="text-align: left">Password:</th><td style="text-align: left"><input type="password" name="j_password"/></td></tr>
                                    <tr><th>&nbsp;</th><td style="text-align: left"><input type="submit" value="login" /> </td></tr>
                                    
                                </table>
                            </div>
                        </td>
                    </tr>
                    <tr style="height: 20"><td colspan="3">&nbsp;</td></tr>
                </table>
            </form>
        </div>
    </body>
</html>
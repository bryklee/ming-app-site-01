<%
long expire = 60*60*24*365;
response.setHeader("Pragma", "public");
response.setHeader("Cache-Control", "max-age=" + expire);
response.setDateHeader("Expires", System.currentTimeMillis() + expire);
%>
<script src="//connect.facebook.net/en_US/all.js"></script>
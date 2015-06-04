<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title><spring:message code="label.acceptancetest.list.page.title"/></title>
</head>
<body>
    <h1><spring:message code="label.acceptancetest.list.page.title"/></h1>
    <div>
        <a href="/acceptancetest/add" id="add-button" class="btn btn-primary"><spring:message code="label.add.acceptancetest.link"/></a>
    </div>
    <div id="acceptancetest-list" class="page-content">
        <c:choose>
            <c:when test="${empty acceptancetests}">
                <p><spring:message code="label.acceptancetest.list.empty"/></p>
            </c:when>
            <c:otherwise>
                <c:forEach items="${ acceptancetests}" var="acceptancetest">
                    <div class="well well-small">
                        <a href="/acceptancetest/${acceptancetest.id}"><c:out value="${acceptancetest.title}"/></a>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>
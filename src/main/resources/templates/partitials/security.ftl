<#assign isAuthorized = Session.SPRING_SECURITY_CONTEXT??>

<#if isAuthorized>
    <#assign
    user = Session.SPRING_SECURITY_CONTEXT.authentication.principal
    username = user.getUsername()
    isAdmin = user.isAdmin()
    isEmployee = user.isEmployee()>
<#else>
    <#assign name = "unknown" isAdmin = false isEmployee = false>
</#if>
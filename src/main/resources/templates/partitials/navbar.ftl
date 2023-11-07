<#macro adminNavbar>
    <nav class="navbar navbar-expand navbar-dark bg-primary justify-content-between">
        <div class="container-fluid">
            <a class="navbar-brand" href="/">Мир-отель</a>
            <ul class="navbar-nav">
                <li class="nav-item">
                    <a class="nav-link link-light" href="/admin/users">Пользователи</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link link-light" href="/admin/registration-keys">Реферальные ключи</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link link-light" href="/logout">Выход</a>
                </li>
            </ul>
        </div>
    </nav>
</#macro>

<#macro employeeNavbar>
    <nav class="navbar navbar-expand navbar-dark bg-primary justify-content-between">
        <div class="container-fluid">
            <a class="navbar-brand" href="/">Мир-отель</a>
            <ul class="navbar-nav">
                <#--<li class="nav-item">
                    <a class="nav-link link-light" href="/info">Данные</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link link-light" href="/orders">Запросы</a>
                </li>-->
                <li class="nav-item">
                    <a class="nav-link link-light" href="/logout">Выход</a>
                </li>
            </ul>
        </div>
    </nav>
</#macro>

<#macro userNavbar>
    <nav class="navbar navbar-expand navbar-dark bg-primary justify-content-between">
        <div class="container-fluid">
            <a class="navbar-brand" href="/">Мир-отель</a>
            <ul class="navbar-nav">
                <#--<li class="nav-item">
                    <a class="nav-link link-light" href="/orders-view">Запросы</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link link-light" href="/profile">Профиль</a>
                </li>-->
                <li class="nav-item">
                    <a class="nav-link link-light" href="/logout">Выход</a>
                </li>
            </ul>
        </div>
    </nav>
</#macro>

<#macro notAuthorizedNavbar>
    <nav class="navbar navbar-expand navbar-dark bg-primary justify-content-between">
        <div class="container-fluid">
            <a class="navbar-brand" href="/">Мир-отель</a>
            <ul class="navbar-nav">
                <li class="nav-item">
                    <a class="nav-link link-light" href="/login">Вход</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link link-light" href="/registration-user">Регистрация</a>
                </li>
            </ul>
        </div>
    </nav>
</#macro>


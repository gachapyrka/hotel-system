<#import "partitials/common.ftl" as common>

<@common.page title="Регистрация">
    <div class="container">
        <div class="row mt-5">
            <div class="col text-center">
                <h4>Регистрация</h4>
            </div>
        </div>
        <form id="registrationForm" action="/login" method="post">
            <div class="row mt-4">
                <div class="col-8 col-md-6 offset-2 offset-md-3">
                    <div class="form-floating">
                        <input class="form-control" type="email" id="emailInput" required="true" name="username"/>
                        <label for="emailInput">Эл. почта</label>
                    </div>
                </div>
            </div>

            <div class="row mt-2">
                <div class="col-8 col-md-6 offset-2 offset-md-3">
                    <div class="form-floating">
                        <input class="form-control" type="password" id="passwordInput" required="true" name="password"/>
                        <input type="hidden" name="_csrf" value="${_csrf.token}" />
                        <label for="passwordInput">Пароль</label>
                    </div>
                </div>
            </div>

            <div class="row mt-2">
                <div class="col-8 col-md-6 offset-2 offset-md-3">
                    <div class="form-floating">
                        <input class="form-control" type="password" id="repeatPasswordInput" required="true" name="repeatPassword"/>
                        <label id="repeatPasswordInputLabel" for="repeatPasswordInput">Повторите пароль</label>
                    </div>
                </div>
            </div>

            <div class="row mt-2">
                <div class="col-8 col-md-6 offset-2 offset-md-3">
                    <div class="form-floating">
                        <input class="form-control" type="text" id="credentialsInput" required="true" name="credentials"/>
                        <label for="credentialsInput">ФИО</label>
                    </div>
                </div>
            </div>

            <div class="row mt-2">
                <div class="col-8 col-md-6 offset-2 offset-md-3">
                    <div class="form-floating">
                        <input class="form-control" type="text" id="passportInput" required="true" name="passport"/>
                        <label id="passportInputLabel" for="passportInput">Номер паспорта</label>
                    </div>
                </div>
            </div>

            <div class="d-flex mt-2 justify-content-center">
                <button class="btn btn-primary" onclick="validate()" type="button">Регистрация</button>
            </div>
        </form>

        <div class="d-flex mt-2 justify-content-center">
            <a href="/registration-employee" class="link-secondary text-center align-text-bottom">Я представитель отеля</a>
        </div>

        <script>
            function validate() {
                var password = document.getElementById("passwordInput").value;
                var passwordRepeat = document.getElementById("repeatPasswordInput").value;

                if(password != passwordRepeat){
                    document.getElementById("repeatPasswordInput").classList.add("is-invalid");
                    document.getElementById("repeatPasswordInputLabel").textContent  = "Пароли должны совпадать!";
                    document.getElementById("repeatPasswordInputLabel").classList.add("text-danger");
                    return;
                }
                else{
                    document.getElementById("repeatPasswordInput").classList.remove("is-invalid");
                    document.getElementById("repeatPasswordInputLabel").textContent  = "Повторите пароль";
                    document.getElementById("repeatPasswordInputLabel").classList.remove("text-danger");
                }

                var passportInput = document.getElementById('passportInput').value;
                var passportRegex = /^[A-Za-zА-Яа-я]{2}\d{7}$/;

                if (!passportRegex.test(passportInput)) {
                    document.getElementById("passportInput").classList.add("is-invalid");
                    document.getElementById("passportInputLabel").textContent  = "Паспорт должен быть в формате AB1234567!";
                    document.getElementById("passportInputLabel").classList.add("text-danger");
                    return;
                }
                else {
                    document.getElementById("passportInput").classList.remove("is-invalid");
                    document.getElementById("passportInputLabel").textContent  = "Номер паспорта";
                    document.getElementById("passportInputLabel").classList.remove("text-danger");
                }

                document.getElementById("registrationForm").submit();
            }
        </script>
    </div>
</@common.page>
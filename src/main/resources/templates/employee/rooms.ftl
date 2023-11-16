<#import "../partitials/common.ftl" as common>

<@common.page title="Управление номерами">
    <div class="row mt-5">
        <div class="col text-center">
            <h4>Номера:</h4>
        </div>
    </div>

    <#if roomTypes??>

        <div class="container mt-3">
            <#if count!=0>
                <table class="table table-striped table-hover table-sm">
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>Номер</th>
                        <th>Тип</th>
                        <th>Статус</th>
                        <th></th>
                    </tr>
                    </thead>
                    <#assign i=1>
                    <#list roomTypes as typ>
                        <#list typ.rooms as r>
                            <tr>
                                <th class="col-1" scope="row">${i}.</th>
                                <#assign i=i+1>
                                <td class="col-2">${r.number}</td>
                                <td class="col-3">${typ.name}</td>
                                <#if r.isLocked()>
                                    <td class="col-2">Занят</td>
                                    <td class="col-1">
                                        <form method="post" action="/employee/room/delete/${r.id}">
                                            <input type="submit" class="btn btn-sm btn-primary" disabled value="Удалить">
                                            <input type="hidden" name="_csrf" value="${_csrf.token}" />
                                        </form>
                                    </td>
                                <#else>
                                    <td class="col-2">Свободен</td>
                                    <td class="col-1">
                                        <form method="post" action="/employee/room/delete/${r.id}">
                                            <input type="submit" class="btn btn-sm btn-primary" value="Удалить">
                                            <input type="hidden" name="_csrf" value="${_csrf.token}" />
                                        </form>
                                    </td>
                                </#if>
                            </tr>
                        </#list>
                    </#list>
                </table>
            <#else>
                <div class="col text-center">
                    <h6>Нет ни одного номера.</h6>
                </div>
            </#if>
        </div>
        <form method="post" id="addRoomForm" action="/employee/room/add">
            <div class="container mt-5">
                <div class="row align-items-center justify-content-start">
                    <div class="col-auto">
                        <label for="newsletterInput" id="newsletterInputLabel" class="col-form-label">Добавить номер:</label>
                    </div>
                </div>
                <div class="row align-items-center justify-content-start">
                    <div class="col-auto">
                        <div class="form-floating">
                            <#if errorText??>
                                <input class="form-control is-invalid" type="text" id="nameInput" required placeholder="Номер" name="name" value="${name}"/>
                                <label id="nameInputLabel" for="nameInput" class="text-danger">${errorText}</label>
                            <#else>
                                <input class="form-control" type="text" id="nameInput" required placeholder="Номер" name="name"/>
                                <label id="nameInputLabel" for="nameInput">Номер</label>
                            </#if>
                        </div>
                    </div>
                </div>
                <div class="row align-items-center justify-content-start mt-2">
                    <div class="col-auto">
                        <select class="form-select" name="roomTypeId" required aria-label="Тип номера">
                            <option selected disabled>Выберите тип номера</option>
                            <#list roomTypes as r>
                                <option value="${r.id}">${r.name}</option>
                            </#list>
                        </select>
                    </div>
                </div>
                <div class="row align-items-center justify-content-start mt-2">
                    <div class="col-auto">
                        <button class="btn btn-primary" id="newsletterButton" onclick="validate()" type="button">Отправить</button>
                        <input type="hidden" name="_csrf" value="${_csrf.token}" />
                    </div>
                </div>
            </div>
        </form>
    <#else>
        <div class="row mt-5">
            <div class="col text-center">
                <h6>Нет ни одного типа номеров.</h6>
                <a class="btn btn-primary" href="/employee/hotel">Добавить тип номера</a>
            </div>
        </div>
    </#if>

    <script>
        function isEmpty(str) {
            return !str.trim().length;
        }

        function onSearch() {

            var search = document.getElementById("searchInput").value;
            var request = search == null? "//localhost:8080/admin/users" :
                "//localhost:8080/admin/users?search="+search;

            window.location.href = request;
        }

        function validate() {
            var name = document.getElementById("nameInput").value;
            if(isEmpty(name)){
                document.getElementById("nameInput").classList.add("is-invalid");
                document.getElementById("nameInputLabel").textContent  = "Заполните поле!";
                document.getElementById("nameInputLabel").classList.add("text-danger");
                return;
            }
            else{
                document.getElementById("nameInput").classList.remove("is-invalid");
                document.getElementById("nameInputLabel").textContent  = "Номер";
                document.getElementById("nameInputLabel").classList.remove("text-danger");
            }

            document.getElementById("addRoomForm").submit();
        }
    </script>
</@common.page>
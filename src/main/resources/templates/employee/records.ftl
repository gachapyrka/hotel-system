<#import "../partitials/common.ftl" as common>

<@common.page title="Забронированные номера">
    <div class="row mt-5">
        <div class="col text-center">
            <h4>Забронированные номера:</h4>
        </div>
    </div>

    <div class="container mt-3">
        <div class="row g-2 align-items-center justify-content-between">
            <div class="col-5">
                <div class="row g-2 align-items-center justify-content-start">
                    <div class="col-auto">
                        <a class="btn btn-primary disabled" href="/employee/records">Текущие</a>
                    </div>
                    <div class="col-auto">
                        <a class="btn btn-secondary" href="/employee/history">История</a>
                    </div>
                </div>
            </div>
            <div class="col-7">
                <div class="row g-2 align-items-center justify-content-end">
                    <div class="col-5 col-lg-3">
                        <#if search??>
                            <input type="text" id="searchInput" class="form-control" placeholder="Поиск..." value="${search}">
                        <#else>
                            <input type="text" id="searchInput" class="form-control" placeholder="Поиск...">
                        </#if>
                    </div>
                    <div class="col-auto">
                        <button class="btn btn-sm btn-primary" onclick="onSearch()" type="button">Поиск</button>
                        <input type="hidden" name="_csrf" value="${_csrf.token}" />
                    </div>
                </div>
            </div>
        </div>
    </div>

    <#if records??>

        <div class="container mt-3">
            <table class="table table-striped table-hover table-sm">
                <thead>
                <tr>
                    <th>#</th>
                    <th>Email</th>
                    <th>ФИО</th>
                    <th>Телефон</th>
                    <th>Номер</th>
                    <th>Даты</th>
                    <th>Состояние</th>
                    <th></th>
                </tr>
                </thead>
                <#list records as r>
                    <tr>
                        <th class="col-1" scope="row">${r_index + 1}.</th>
                        <td class="col-1">${r.userProfile.account.username}</td>
                        <td class="col-1">${r.userProfile.credentials}</td>
                        <td class="col-1">${r.userProfile.telephone}</td>
                        <td class="col-1">${r.room.number}</td>
                        <td class="col-2">${r.getLocalStartDate()} - ${r.getLocalEndDate()}</td>
                        <form method="post" action="/employee/record/${r.id}">
                        <td class="col-2">
                            <select class="form-select" name="status" required aria-label="Статус">
                                <option selected value="0">Активен</option>
                                <option value="1">Отменен</option>
                                <option value="2">Завершен</option>
                            </select>
                        </td>
                            <td class="col-1">
                                <input type="submit" class="btn btn-sm btn-primary" value="Обновить">
                                <input type="hidden" name="_csrf" value="${_csrf.token}" />
                            </td>
                        </form>
                    </tr>
                </#list>
            </table>
        </div>
    <#else>
        <div class="row mt-5">
            <div class="col text-center">
                <h6>Список забронированных номеров пуст</h6>
            </div>
        </div>
    </#if>

    <script>
        function isEmpty(str) {
            return !str.trim().length;
        }

        function onSearch() {

            var search = document.getElementById("searchInput").value;
            var request = search == null? "//localhost:8080/employee/records" :
                "//localhost:8080/employee/records?search="+search;

            window.location.href = request;
        }
    </script>
</@common.page>
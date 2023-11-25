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
                        <a class="btn btn-secondary" href="/user/records">Текущие</a>
                    </div>
                    <div class="col-auto">
                        <a class="btn btn-primary disabled" href="/user/history">История</a>
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
                    <th>Отель</th>
                    <th>Номер</th>
                    <th>Тип номера</th>
                    <th>Даты</th>
                    <th>Статус</th>
                </tr>
                </thead>
                <#list records as r>
                    <tr>
                        <th class="col-1" scope="row">${r_index + 1}.</th>
                        <td class="col-2">${r.room.roomType.hotel.name}</td>
                        <td class="col-2">${r.room.number}</td>
                        <td class="col-2">${r.room.roomType.name}</td>
                        <td class="col-3">${r.getLocalStartDate()} - ${r.getLocalEndDate()}</td>
                        <td class="col-2">
                            <#if r.isCancelled()>
                                Отменен
                            <#else>
                                Завершен
                            </#if>
                        </td>
                    </tr>
                </#list>
            </table>
            <div class="mt-3">
                <a onclick="onReport()" class="btn btn-primary btn-lg">Отправить на почту</a>
            </div>
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
            var request = search == null? "//localhost:8080/user/history" :
                "//localhost:8080/user/history?search="+search;

            window.location.href = request;
        }

        function onReport() {

            var search = document.getElementById("searchInput").value;
            var request = search == null? "//localhost:8080/user/report" :
                "//localhost:8080/user/report?search="+search;

            window.location.href = request;
        }
    </script>
</@common.page>
<#import "../partitials/common.ftl" as common>

<@common.page title="Мои заявки">
    <div class="row mt-5">
        <div class="col text-center">
            <h4>Заявки:</h4>
        </div>
    </div>

    <div class="container mt-3">
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

    <#if orders??>

        <div class="container mt-3">
            <table class="table table-striped table-hover table-sm">
                <thead>
                <tr>
                    <th>#</th>
                    <th>Отель</th>
                    <th>Тип номера</th>
                    <th>Даты</th>
                    <th>Описание</th>
                    <th></th>
                </tr>
                </thead>
                <#list orders as o>
                    <tr>
                        <th class="col-1" scope="row">${o_index + 1}.</th>
                        <td class="col-2">${o.roomType.hotel.name}</td>
                        <td class="col-2">${o.roomType.name}</td>
                        <td class="col-3">${o.getLocalStartDate()} - ${o.getLocalEndDate()}</td>
                        <td class="col-3">${o.description}</td>
                        <td class="col-1">
                            <form method="post" action="/user/order/delete/${o.id}">
                                <input type="submit" class="btn btn-sm btn-danger" value="Отменить">
                                <input type="hidden" name="_csrf" value="${_csrf.token}" />
                            </form>
                        </td>
                    </tr>
                </#list>
            </table>
        </div>
    <#else>
        <div class="row mt-5">
            <div class="col text-center">
                <h6>Список заявок пуст</h6>
            </div>
        </div>
    </#if>

    <script>
        function isEmpty(str) {
            return !str.trim().length;
        }

        function onSearch() {

            var search = document.getElementById("searchInput").value;
            var request = search == null? "//localhost:8080/user/orders" :
                "//localhost:8080/user/orders?search="+search;

            window.location.href = request;
        }
    </script>
</@common.page>
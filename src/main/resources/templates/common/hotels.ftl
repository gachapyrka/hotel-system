<#import "../partitials/common.ftl" as common>

<@common.page title="Отели">
    <div class="row mt-5">
        <div class="col text-center">
            <h4>Отели:</h4>
        </div>
    </div>

    <div class="container mt-3">
        <div class="row align-items-center justify-content-between">
            <div class="row g-4 align-items-center justify-content-end">
                <div class="col-5 col-lg-3">
                    <#if name??>
                        <input type="text" id="searchInput" class="form-control" placeholder="Поиск..." value="${name}">
                    <#else>
                        <input type="text" id="searchInput" class="form-control" placeholder="Поиск...">
                    </#if>
                </div>
                <div class="col-5 col-lg-3">
                    <#if minCost??>
                        <input type="number" min="0" id="minCostInput" class="form-control" placeholder="Мин. цена" value="${minCost}">
                    <#else>
                        <input type="number" min="0" id="minCostInput" class="form-control" placeholder="Мин. цена">
                    </#if>
                </div>
                <div class="col-5 col-lg-3">
                    <#if maxCost??>
                        <input type="number" min="0" id="maxCostInput" class="form-control" placeholder="Макс. цена" value="${maxCost}">
                    <#else>
                        <input type="number" min="0" id="maxCostInput" class="form-control" placeholder="Макс. цена">
                    </#if>
                </div>
                <div class="col-auto">
                    <button class="btn btn-sm btn-primary" onclick="onSearch()" type="button">Поиск</button>
                    <input type="hidden" name="_csrf" value="${_csrf.token}" />
                </div>
            </div>
        </div>
    </div>

    <#if hotels??>

        <div class="container mt-3">
            <table class="table table-striped table-hover table-sm">
                <thead>
                <tr>
                    <th>#</th>
                    <th></th>
                    <th>Название</th>
                    <th>Цены</th>
                    <th>Всего номеров</th>
                    <th>Свободно номеров</th>
                    <th></th>
                </tr>
                </thead>
                <#list hotels as h>
                    <tr>
                        <th class="col-1" scope="row">${h_index + 1}.</th>
                        <td class="col-3">
                            <#if h.getFirstImage()??>
                                <img src="/img/${h.getFirstImage().path}" height="100" width="200">
                            </#if>
                        </td>
                        <td class="col-3">${h.name}</td>
                        <td class="col-2">от ${h.getMinCost()} до ${h.getMaxCost()} руб/сут</td>
                        <td class="col-1">${h.getRoomsCount()}</td>
                        <td class="col-1">${h.getFreeRoomsCount()}</td>
                        <td class="col-1">
                            <form method="get" action="/all/hotel/${h.id}">
                                <input type="submit" class="btn btn-sm btn-primary" value="Подробнее">
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
                <h6>Список пуст</h6>
            </div>
        </div>
    </#if>

    <script>
        function isEmpty(str) {
            return !str.trim().length;
        }

        function onSearch() {

            var search = document.getElementById("searchInput").value;
            var minCost = document.getElementById("minCostInput").value;
            var maxCost = document.getElementById("maxCostInput").value;
            var request = "//localhost:8080/all/hotels";
            if(search != null || minCost != null || maxCost != null)
            {
                request += "?";
                var isFirst = true;
                if(search != null){
                    request += "name="+search;
                    isFirst = false;
                }
                if(minCost!= null){
                    if(!isFirst)
                        request += "&";
                    request+="minCost="+minCost;
                    isFirst = false;
                }
                if(maxCost!= null){
                    if(!isFirst)
                        request += "&";
                    request+="maxCost="+maxCost;
                    isFirst = false;
                }
            }

            window.location.href = request;
        }
    </script>
</@common.page>
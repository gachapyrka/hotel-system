<#import "../partitials/common.ftl" as common>
<#import "../partitials/carousel.ftl" as carousels>

<@common.page title="Управление отелем">
    <div class="container mt-5">
        <div class="row g-2">
            <div class="col-7">
                <div class="row">
                    <#if hotelImages??>
                        <@carousels.carousel images=hotelImages/>
                    <#else>
                        <@carousels.noImageCarousel/>
                    </#if>
                </div>
            </div>

            <div class="col-4 offset-1">
                <div class="row">
                    <h2>Отель ${hotel.name}</h2>
                </div>
                <div class="row mt-2">
                    <h6>От ${hotel.getMinCost()} до ${hotel.getMaxCost()} руб./сут.</h6>
                </div>
                <div class="mt-2">
                    <a class="btn btn-primary" href="/employee/hotel/edit">Редактировать</a>
                </div>
            </div>
        </div>
    </div>
    <div class="container">
        <div class="row mt-5 text-center">
            <#if description??>
                <h5>${description}</h5>
            <#else>
                <h5>Здесь будет ваше описание.</h5>
            </#if>
        </div>
    </div>
    <div class="container">
        <div class="row mt-5">
            <h5>Цены за номера:</h5>
            <#if roomTypes??>
                <table class="table table-striped table-hover table-sm">
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>Название</th>
                        <th>Стоимость</th>
                        <th>Всего номеров</th>
                        <th>Свободно</th>
                        <th></th>
                        <th></th>
                    </tr>
                    </thead>
                    <#list roomTypes as r>
                        <tr>
                            <th class="col-1" scope="row">${r_index + 1}.</th>
                            <td class="col-2">${r.name}</td>
                            <td class="col-2">${r.costPerDay} руб./д.</td>
                            <td class="col-2">${r.getRoomsCount()}</td>
                            <td class="col-3">${r.getFreeRoomsCount()}</td>
                            <td class="col-1">
                                <#if r.getFreeRoomsCount() == r.getRoomsCount()>
                                    <form method="post" action="/employee/room-type/delete/${r.id}">
                                        <input type="submit" class="btn btn-sm btn-danger" value="Удалить">
                                        <input type="hidden" name="_csrf" value="${_csrf.token}" />
                                    </form>
                                <#else>
                                    <input type="submit" class="btn btn-sm btn-danger" disabled value="Удалить">
                                </#if>
                            </td>
                            <td class="col-1">
                                <form method="get" action="/employee/room-type/${r.id}">
                                    <input type="submit" class="btn btn-sm btn-primary" value="Подробнее">
                                    <input type="hidden" name="_csrf" value="${_csrf.token}" />
                                </form>
                            </td>
                        </tr>
                    </#list>
                </table>
                <form method="post" action="/employee/room-type/add">
                    <div class="row g1 mt-2 justify-content-end align-items-center">
                        <div class="col-auto">
                            <button class="btn btn-primary" type="submit">Добавить</button>
                            <input type="hidden" name="_csrf" value="${_csrf.token}" />
                        </div>
                    </div>
                </form>
            <#else>
                <h6 class="text-center">Пока что нет ни одного типа номеров</h6>
                <form method="post" action="/employee/room-type/add">
                    <div class="row g1 mt-2 justify-content-center align-items-center">
                        <div class="col-auto">
                            <button class="btn btn-primary" type="submit">Добавить</button>
                            <input type="hidden" name="_csrf" value="${_csrf.token}" />
                        </div>
                    </div>
                </form>
            </#if>
        </div>
    </div>
    <div class="container">
        <div class="row mt-5 mb-5">
            <h5>Отзывы:</h5>
            <#if comments??>
                <#list comments as c>
                    <div class="row mt-3">
                        <div class="col">
                            <div class="border border-primary-subtle rounded-3">
                                <div class="row justify-content-start">
                                    <div style="margin-left: 2%">
                                        <p class="fw-bold">${c.title}</p>
                                        <p>${c.text}</p>
                                    </div>
                                </div>
                                <div class="text-end fw-italic">
                                    ${c.userProfile.credentials} - ${c.getLocalCreationDate()}
                                </div>
                            </div>
                        </div>
                    </div>
                </#list>
            <#else>
                <h6 class="text-center">Пока что нет ни одного отзыва</h6>
            </#if>
        </div>
    </div>
</@common.page>
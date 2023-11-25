<#import "../partitials/common.ftl" as common>
<#import "../partitials/carousel.ftl" as carousels>

<@common.page title="${hotel.name}">
    <div class="container mt-3">
        <div class="row">
            <div class="col-auto">
                <a class="btn btn-primary" href="/all/hotels">Назад</a>
            </div>
        </div>
    </div>
    <div class="container mt-3">
        <div class="row g-2">
            <div class="col-7">
                <div class="row">
                    <#if hotelImages??>
                        <@carousels.carousel images=hotelImages/>
                    <#else>
                        <@carousels.noImageCommonCarousel/>
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
                    <a class="btn btn-primary btn-lg" href="/user/hotel/${hotel.id}/borrow">Записаться</a>
                </div>
            </div>
        </div>
    </div>
    <div class="container">
        <div class="row mt-5 text-center">
            <#if description??>
                <h5>${description}</h5>
            <#else>
                <h5>Увы, пока что нет описания.</h5>
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
                    </tr>
                    </thead>
                    <#list roomTypes as r>
                        <tr>
                            <th class="col-1" scope="row">${r_index + 1}.</th>
                            <td class="col-6">${r.name}</td>
                            <td class="col-2">${r.costPerDay} руб./д.</td>
                            <td class="col-1">${r.getRoomsCount()}</td>
                            <td class="col-1">${r.getFreeRoomsCount()}</td>
                            <td class="col-1">
                                <form method="get" action="/all/room-type/${r.id}">
                                    <input type="submit" class="btn btn-sm btn-primary" value="Подробнее">
                                    <input type="hidden" name="_csrf" value="${_csrf.token}" />
                                </form>
                            </td>
                        </tr>
                    </#list>
                </table>
            <#else>
                <h6 class="text-center">Пока что нет ни одного типа номеров</h6>
            </#if>
        </div>
    </div>
    <div class="container">
        <div class="row mt-5 mb-5">
            <h5>Отзывы:</h5>
            <div class="row align-items-center justify-content-start mt-2">
                <div class="col-auto">
                    <a href="/user/comment/add/${hotel.id}" class="btn btn-primary">Написать отзыв</a>
                </div>
            </div>
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
<#import "../partitials/common.ftl" as common>
<#import "../partitials/carousel.ftl" as carousels>

<@common.page title=roomType.name>
    <div class="container mt-5">
        <div class="row g-2">
            <div class="col-7">
                <div class="row">
                    <#if roomImages??>
                        <@carousels.carousel images=roomImages/>
                    <#else>
                        <@carousels.noImageCarousel/>
                    </#if>
                </div>
            </div>

            <div class="col-4 offset-1">
                <div class="row">
                    <h2>${roomType.name}</h2>
                </div>
                <div class="row mt-2">
                    <h6>${roomType.costPerDay} руб./сут.</h6>
                </div>
                <div class="mt-2">
                    <a class="btn btn-primary" href="/employee/hotel">Назад</a>
                </div>
                <div class="mt-2">
                    <a class="btn btn-primary" href="/employee/room-type/edit/${roomType.id}">Редактировать</a>
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
</@common.page>
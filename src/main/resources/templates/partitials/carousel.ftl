<#macro carousel images>
    <div id="carousel" class="carousel slide">
        <div class="carousel-inner">
            <#list images as i>
                <#if i_index == 0>
                    <div class="carousel-item active">
                        <img src="/img/${i.path}" class="d-block w-100" height="400">
                    </div>
                <#else>
                    <div class="carousel-item">
                        <img src="/img/${i.path}" class="d-block w-100" height="400">
                    </div>
                </#if>
            </#list>
        </div>
        <button class="carousel-control-prev" type="button" data-bs-target="#carousel" data-bs-slide="prev">
            <span class="carousel-control-prev-icon" aria-hidden="true"></span>
            <span class="visually-hidden">Предыдущий</span>
        </button>
        <button class="carousel-control-next" type="button" data-bs-target="#carousel" data-bs-slide="next">
            <span class="carousel-control-next-icon" aria-hidden="true"></span>
            <span class="visually-hidden">Следующий</span>
        </button>
    </div>
</#macro>

<#macro noImageCarousel>
    <div id="carousel" class="carousel slide">
        <div class="carousel-inner">
            <div class="carousel-item active">
                <img src="/img/noimage.jpg" class="d-block w-100" height="400">
            </div>
            <div class="carousel-item">
                <img src="/img/noimage.jpg" class="d-block w-100" height="400">
            </div>
        </div>
        <button class="carousel-control-prev" type="button" data-bs-target="#carousel" data-bs-slide="prev">
            <span class="carousel-control-prev-icon" aria-hidden="true"></span>
            <span class="visually-hidden">Предыдущий</span>
        </button>
        <button class="carousel-control-next" type="button" data-bs-target="#carousel" data-bs-slide="next">
            <span class="carousel-control-next-icon" aria-hidden="true"></span>
            <span class="visually-hidden">Следующий</span>
        </button>
    </div>
</#macro>
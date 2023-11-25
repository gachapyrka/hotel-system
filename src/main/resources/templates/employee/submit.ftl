<#import "../partitials/common.ftl" as common>

<@common.page title="Заселение">
    <div class="container">
        <div class="row mt-5">
            <div class="col text-center">
                <h4>Заселение</h4>
            </div>
        </div>
        <form id="submitForm" action="/employee/order/${orderId}/submit" method="post">
            <div class="row mt-2">
                <div class="col-8 col-md-6 offset-2 offset-md-3">
                    <select class="form-select" name="roomId" required aria-label="Номер">
                        <option selected disabled>Выберите номер</option>
                        <#list rooms as r>
                            <option value="${r.id}">${r.number}</option>
                        </#list>
                    </select>
                </div>
            </div>

            <div class="d-flex mt-2 justify-content-center">
                <input type="hidden" name="_csrf" value="${_csrf.token}" />
                <button class="btn btn-primary" onclick="validate()" type="button">Подтвердить</button>
            </div>
        </form>

        <div class="d-flex mt-2 justify-content-center">
            <a href="/employee/orders" class="link-secondary text-center align-text-bottom">Отмена</a>
        </div>

        <script>
            function validate() {
                document.getElementById("submitForm").submit();
            }
        </script>
    </div>
</@common.page>
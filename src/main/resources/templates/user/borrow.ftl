<#import "../partitials/common.ftl" as common>

<@common.page title="Бронирование">
    <div class="container">
        <div class="row mt-5">
            <div class="col text-center">
                <h4>Бронирование</h4>
            </div>
        </div>
        <form id="borrowForm" action="/user/hotel/${hotelId}/borrow" method="post">
            <div class="row mt-2">
                <div class="col-8 col-md-6 offset-2 offset-md-3">
                    <select class="form-select" name="roomTypeId" required aria-label="Тип номера">
                        <option selected disabled>Выберите тип номера</option>
                        <#list roomTypes as r>
                            <option value="${r.id}">${r.name}</option>
                        </#list>
                    </select>
                </div>
            </div>

            <div class="row mt-2">
                <div class="col-8 col-md-6 offset-2 offset-md-3">
                    <div class="form-floating">
                        <input class="form-control" type="date" id="dateInput" required placeholder="date" name="endDate"/>
                        <label id="dateInputLabel" for="dateInput">До какого числа бронь?</label>
                    </div>
                </div>
            </div>

            <div class="row mt-2">
                <div class="col-8 col-md-6 offset-2 offset-md-3">
                    <div class="form-floating">
                        <input class="form-control" type="text" id="descriptionInput" placeholder="description" name="description"/>
                        <label id="descriptionInputLabel" for="descriptionInput">Доп. данные</label>
                    </div>
                </div>
            </div>

            <div class="d-flex mt-2 justify-content-center">
                <input type="hidden" name="_csrf" value="${_csrf.token}" />
                <button class="btn btn-primary" onclick="validate()" type="button">Оставить заявку</button>
            </div>
        </form>

        <div class="d-flex mt-2 justify-content-center">
            <a href="/all/hotel/${hotelId}" class="link-secondary text-center align-text-bottom">Отмена</a>
        </div>

        <script>
            let today = new Date();

            let tomorrow = new Date(today);
            tomorrow.setDate(tomorrow.getDate() + 1);

            let max = new Date(today);
            max.setDate(max.getDate() + 60);

            let formattedDateTomorrow = tomorrow.toISOString().split('T')[0];
            document.getElementById('dateInput').min = formattedDateTomorrow;

            let formattedDateMax = max.toISOString().split('T')[0];
            document.getElementById('dateInput').max = formattedDateMax;
        </script>

        <script>
            function isEmpty(str) {
                return !str.trim().length;
            }

            function validate() {
                document.getElementById("borrowForm").submit();
            }
        </script>
    </div>
</@common.page>
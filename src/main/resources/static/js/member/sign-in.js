APP_ITA = function () {
    return {
        init: function () {
            alert('init');
        },
        doLogin: function () {
            console.log('doLogin');

            $.ajax({
                url: "login",
                method: 'post',
                data: $('#js-form-login').serialize(),
                dataType: "JSON",
            }).done(function () {
                $(this).addClass("done");
            });
        }
    }
}()

$(document).ready(function () {
    $('#js-btn-login').click(function () {
        // console.log('click');
        // APP_ITA.doLogin();
    })

    APP_ITA.init();
});

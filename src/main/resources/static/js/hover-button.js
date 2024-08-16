$(document).ready(function () {
    let buttonRemove = $('.button-remove');
    function removeSuccess() {
        buttonRemove.removeClass('success');
    }

    buttonRemove.click(function () {
        $(this).addClass('success');
        setTimeout(removeSuccess, 3000);
    })
})
$(document).ready(function () {
    $('.form-header .patient-code').on('click', function (e) {
        let textToCopy = $(this).attr('code-value');
        copyToClipboard(textToCopy);
    });

    function copyToClipboard(textToCopy) {
        let tempElement = $('<textarea>').val(textToCopy).appendTo('body').select();
        document.execCommand('copy');
        tempElement.remove();

        // Show a confirmation message
        $('.copy-success').fadeIn().delay(1000).fadeOut();
    }
});
$(document).ready(function () {
    let url;
    $('.delete').click(function () {
        url = $(this).attr('data-post');
        $("#confirmModal").fadeIn();
    });

    $("#confirmYes").on("click", function () {
        if (url) {
            $.ajax({
                url: url,
                type: 'GET',
                success: function (response) {
                    $("#confirmModal").fadeOut();
                    $("#successPopup").fadeIn();
                },
                error: function (error) {
                    $("#confirmModal").fadeOut();
                    $("#errorPopup").fadeIn();
                }
            });
        }
        $("#confirmModal").fadeOut();
    });

    $("#confirmNo").on("click", function () {
        $("#confirmModal").fadeOut();
    });

    $("#successOk").on("click", function () {
        $("#successPopup").fadeOut();
    });

    $("#errorOk").on("click", function () {
        $("#errorPopup").fadeOut();
    });
});
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
                    $("#confirmModal").fadeOut(); // Hide confirmation modal
                    $("#successPopup").fadeIn(); // Show success popup
                },
                error: function (error) {
                    $("#confirmModal").fadeOut(); // Hide confirmation modal
                    $("#errorPopup").fadeIn(); // Show error popup
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
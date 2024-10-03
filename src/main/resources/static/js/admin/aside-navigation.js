$(document).ready(function () {
    let navItems = $('#nav-list-wrapper .nav-item');
    let mainContent = $('.main-wrapper');
    mainContent.addClass('show');
    let className = mainContent.attr('data-class-page');
    $('#nav-list-wrapper li[data-class-page="' + className + '"]').addClass('active');

    // navItems.on('click', 'a', function (event) {
    //     event.preventDefault();
    //     navItems.removeClass('active');
    //     $(this).parent().toggleClass('active');
    //     let url = $(this).attr('href');
    //
    //     $.ajax({
    //         url: url,
    //         type: 'GET',
    //         success: function(data) {
    //             window.history.pushState({}, '', url)
    //             let tempDiv = $('<div></div>').html(data);
    //             let newContent = tempDiv.find('.main-content_container');
    //
    //             // Thêm lớp "fade-in-up" cho hiệu ứng xuất hiện
    //             mainContent.replaceWith(newContent);
    //             mainContent = newContent;
    //             setTimeout(function() {
    //                 newContent.addClass('show');
    //             }, 100);
    //         },
    //         error: function() {
    //         }
    //     });
    // });
});
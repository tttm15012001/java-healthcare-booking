$(document).ready(function () {
    const baseUrl = window.location.origin + window.location.pathname;
    $(".action-filter-show").on('click', function (event) {
        event.preventDefault();
        $(this).hide();
        $(".action-filter-close").show();
        $(".wrapper-filter").slideDown();
    });

    $(".action-filter-close").on('click', function (event) {
        event.preventDefault();
        $(this).hide();
        $(".action-filter-show").show();
        $(".wrapper-filter").slideUp();
    });

    $(".action-filter button.do").on('click', function (event) {
        event.preventDefault();
        doFilter();
    });

    $(".action-filter button.clear").on('click', function (event) {
        event.preventDefault();
        let filters = $(".wrapper-filter-options").find('input');
        filters.each(function() {
            $(this).val('');
        });
        doFilter();
    });

    function doFilter() {
        let params = [];

        let filters = $(".wrapper-filter-options").find('input');
        filters.each(function() {
            let input = $(this);
            if (input.val()) {
                params.push({'fieldName': input.attr('name'), 'value': input.val()});
            }
        });

        let queryString = '';
        let isFirst = true;
        params.forEach(function(param) {
            if (!isFirst) {
                queryString += '&';
            }
            isFirst = false;
            queryString += param.fieldName + '=' + param.value;
        })

        let newUrl = baseUrl;
        if (params.length > 0) {
            newUrl += '?' + queryString;
        }

        /* Will be replaced by ajax call */
        window.location.href = newUrl;
    }
});
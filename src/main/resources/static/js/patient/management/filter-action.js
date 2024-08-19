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

        const patientTable = $(".patient-table tbody");
        let filterData = {};

        // Collect the filter values
        filters.each(function() {
            let input = $(this);
            if (input.val()) {
                filterData[input.attr('name')] = input.val();
            }
        });

        /* Will be replaced by ajax call */
        if (newUrl !== window.location.href) {
            $.ajax({
                url: '/admin/patient/management/filter', // Adjust the URL to your filter endpoint
                method: 'GET',
                data: filterData,
                success: function(response) {
                    window.history.pushState({}, '', newUrl);
                    patientTable.empty();

                    if (response.length > 0) {
                        $.each(response, function(index, patient) {
                            let row =
                                `<tr>
                                    <td>${patient.firstName}</td>
                                    <td>${patient.lastName}</td>
                                    <td>${patient.phoneNumber}</td>
                                    <td>${patient.gender}</td>
                                    <td class="actions">
                                        <a href="/admin/patient/management/view/${patient.id}" class="edit"></a>
                                        <a href="/admin/patient/management/delete/${patient.id}" class="delete"></a>
                                    </td>
                                </tr>`;
                            let $newRow = $(row).appendTo(patientTable);
                            let actionEdit = $(".action-template .action-edit").html();
                            let actionDelete = $(".action-template .action-delete").html();
                            $newRow.find('.actions .edit').html(actionEdit);
                            $newRow.find('.actions .delete').html(actionDelete);
                        });
                    } else {
                        // If no patients match the filter criteria
                        patientTable.append('<tr><td colspan="5">No patients found</td></tr>');
                    }
                },
                error: function(error) {
                    console.error('Error during filtering:', error);
                }
            });
            // window.location.href = newUrl;
        }
    }
});
$(document).ready(function () {
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
        $(".form-filter")[0].reset();
        doFilter();
    });

    function getQueryParams() {
        let searchParams = new URLSearchParams(window.location.search);
        let params = {};

        for (let [key, value] of searchParams.entries()) {
            params[key] = value;
        }

        return params;
    }

    // Function to populate form fields with query params
    function populateForm(params) {
        Object.keys(params).forEach(function (key) {
            let input = $(`[name="${key}"]`);
            if (input.length) {
                input.val(params[key]);
            }
        });
    }

    // Populate form fields when page loads
    let params = getQueryParams();
    populateForm(params);

    function doFilter() {
        const patientTable = $(".patient-table tbody");
        let formData = $(".form-filter").serializeArray();
        let params = formData.filter(item => item.value !== '').map(item => ({
            fieldName: item.name,
            value: item.value
        }));

        let queryString = params.map(param => `${param.fieldName}=${encodeURIComponent(param.value)}`).join('&');
        let baseUrl = window.location.origin + '/admin/patient/management';
        let newUrl = queryString ? `${baseUrl}?${queryString}` : baseUrl;

        /* Will be replaced by ajax call */
        if (newUrl !== window.location.href) {
            $.ajax({
                url: '/admin/patient/management/filter',
                method: 'GET',
                data: params.reduce((acc, cur) => ({ ...acc, [cur.fieldName]: cur.value }), {}),
                success: function(response) {
                    window.history.pushState({}, '', newUrl);
                    patientTable.empty();

                    if (response.length > 0) {
                        $.each(response, function(index, patient) {
                            let status = patient.status;
                            let classStatus = getClassStatus(status);
                            let row =
                                `<tr>
                                    <td>${patient.fullName}</td>
                                    <td>${patient.patientCode}</td>
                                    <td>${patient.createdAt}</td>
                                    <td>${patient.gender}</td>
                                    <td>
                                        <div class="status ${classStatus}"><div>
                                    </td>
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

    function getClassStatus(status) {
        let result = '';
        switch (status) {
            case 0:
                result = 'status-new'
                break;
            case 1:
                result = 'status-wait-treatment'
                break;
            case 2:
                result = 'status-treating'
                break;
            case 3:
                result = 'status-waiting-medicine'
                break;
            case 4:
                result = 'status-done'
                break;
            default:
                result = 'status-unknown'
        }

        return result;
    }
});
$(document).ready(function () {
    $(".action-filter-show").on('click', function (event) {
        event.preventDefault();
        $(this).hide();
        $(".action-filter-close").show();
        $('.main-table-content').css('height', 'calc(100% - 60px - 40px - 250px)');
        $(".wrapper-filter").slideDown();
    });

    $(".action-filter-close").on('click', function (event) {
        event.preventDefault();
        $(this).hide();
        $(".action-filter-show").show();
        $(".wrapper-filter").slideUp();
        setTimeout(() => {
            $('.main-table-content').css('height', 'calc(100% - 60px - 40px)');
        }, 250);
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
                contentType: 'application/json',
                dataType: 'json',
                success: function(response) {
                    window.history.pushState({}, '', newUrl);
                    // This is not the optimal solution
                    // let tempDiv = $('<div></div>').html(response);
                    // let filteredContent = tempDiv.find('.main-table-content').html();
                    // $('.main-table-content').html(filteredContent);
                    patientTable.empty();
                    var patientsList = response.patients;

                    var currentPage = response.currentPage;
                    var totalPages = response.totalPages;
                    var totalItems = response.totalItems;
                    var pageSize = response.pageSize;

                    if (patientsList.length > 0) {
                        $.each(patientsList, function(index, patient) {
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

                    let paginationHTML = '';

                    /* handle show previous button */
                    if (currentPage > 0) {
                        paginationHTML += `<a href="/admin/patient/management?page=${currentPage - 1}">Previous</a>`;
                    } else {
                        paginationHTML += `<span style="color: gray;">Previous</span>`;
                    }

                    /* handle show previous 2 pages */
                    for (let i = Math.max(0, currentPage - 2); i < currentPage; i++) {
                        paginationHTML += `<a href="/admin/patient/management?page=${i}" class="page-num">${i + 1}</a>`;
                    }

                    /* handle show current page */
                    paginationHTML += `<span style="font-weight: bold;" class="page-num current">${currentPage + 1}</span>`;

                    /* handle show next 2 pages */
                    for (let i = currentPage + 1; i <= Math.min(currentPage + 2, totalPages - 1); i++) {
                        paginationHTML += `<a href="/admin/patient/management?page=${i}" class="page-num">${i + 1}</a>`;
                    }

                    /* handle show next button */
                    if (currentPage < totalPages - 1) {
                        paginationHTML += `<a href="/admin/patient/management?page=${currentPage + 1}">Next</a>`;
                    } else {
                        paginationHTML += `<span style="color: gray;">Next</span>`;
                    }

                    $('.pagination').html(paginationHTML);
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
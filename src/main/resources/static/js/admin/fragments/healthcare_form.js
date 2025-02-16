$(document).ready(function () {
    /* Trigger show/hide the modal */
    $("#button-request-health-care").click(function () {
        debugger;
        fetchSpecialities();
        fetchDoctors();
        $("#healthcare-modal").show();
    });

    $(document).on('change', '#speciality', function () {
        let speciality = $(this).val();
        fetchDoctors(speciality);
    });

    function fetchSpecialities() {
        $.ajax({
            type: "GET",
            url: "/api/doctor/getAllSpecialities",
            contentType: "application/json",
            success: function (specialities) {
                $("#speciality").empty().append('<option value="">Select Speciality</option>');

                specialities.forEach(speciality => {
                    $("#speciality").append(`<option value="${speciality.code}">${speciality.label}</option>`);
                });
            },
            error: function () {
                console.error("There's something wrong during fetching all specialities");
            }
        });
    }

    function fetchDoctors(speciality = "") {
        let url = speciality ? `/api/doctor/${speciality}` : "/api/doctor/getAll";

        $.ajax({
            type: "GET",
            url: url,
            success: function (doctors) {
                $("#doctor").empty().append('<option value="">Select Doctor</option>');

                doctors.forEach(doctor => {
                    $("#doctor").append(`<option value="${doctor.id}">${doctor.degree}. ${doctor.name}</option>`);
                });
            },
            error: function () {
                console.error("There's something wrong during fetching doctors");
            }
        });
    }

    $(".close").click(function () {
        $("#healthcare-modal").hide();
    });

    /* Toggle doctor selection fields when switching the toggle button */
    $("#select-doctor-toggle").change(function () {
        if ($(this).is(":checked")) {
            $("#doctor-selection-fields").slideDown();
        } else {
            $("#doctor-selection-fields").slideUp();
        }
    });

    /* Add additional fields (for different type of booking) */
    $("#hf-appointment_type").change(function () {
        if ($(this).is(":checked")) {
            $("#another-person-fields").slideDown();
        } else {
            $("#another-person-fields").slideUp();
        }
    });

    /* Handle submit form modal */
    $("#healthcare-form").submit(function (event) {
        event.preventDefault();

        let referenceInformation = {
            name: $("#another-fullName").val(),
            phoneNumber: $("#another-phone-number").val(),
            gender: $("input[name='another-gender']:checked").val()
        }

        let patientInformation = {
            name: $("#hf-fullName").val(),
            phoneNumber: $("#hf-phone-number").val(),
            gender: $("input[name='hf-gender']:checked").val()
        }

        let formData = {
            patient_id: $("#hf-patient_id").val(),
            patient_information: JSON.stringify(patientInformation),
            reference_information: JSON.stringify(referenceInformation),
            appointment_type: $("#hf-appointment_type").val(),
            appointment_time: $("#hf-datetime").val(),
            description: $("#hf-description").val(),
            doctor_id: $('#doctor').find(":selected").val()
        };

        $.ajax({
            type: "POST",
            url: "/api/healthcare-request",
            data: JSON.stringify(formData),
            contentType: "application/json",
            success: function (response) {
                alert("Request submitted successfully!");
                window.location.href = "/admin/weekly-calendar";
            },
            error: function () {
                alert("Failed to submit request. Please try again.");
            }
        });
    });
});
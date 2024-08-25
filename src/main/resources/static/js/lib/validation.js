class FormValidator {
    constructor(form, validationConfig) {
        this.form = form;
        this.validationConfig = validationConfig;
        this.form.addEventListener('submit', this.handleSubmit.bind(this));
    }

    handleSubmit(event) {
        event.preventDefault();
        let isValid = true;
        let firstInvalidElement = null;

        this.validationConfig.forEach(({ className, rules }) => {
            const elements = this.form.querySelectorAll(`.${className}`);

            elements.forEach((element) => {
                let elementIsValid = true;
                let errorMessage = '';

                // Check each rule for the element
                rules.forEach(({ rule, message }) => {
                    if (!validationRules[rule](element.value)) {
                        elementIsValid = false;
                        isValid = false;
                        errorMessage = message;
                        this.showError(element, errorMessage);
                    }
                });

                if (elementIsValid) {
                    this.hideError(element);
                } else if (!firstInvalidElement) {
                    firstInvalidElement = element;
                }
            });
        });

        if (isValid) {
            console.log('Form is valid! Submitting...');
            this.form.submit();
        } else if (firstInvalidElement) {
            firstInvalidElement.focus();
        }
    }

    showError(element, message) {
        let errorElement = element.nextElementSibling;
        if (!errorElement || !errorElement.classList.contains('error-message')) {
            errorElement = document.createElement('div');
            errorElement.className = 'error-message';
            element.parentNode.insertBefore(errorElement, element.nextSibling);
        }
        errorElement.textContent = message;
    }

    hideError(element) {
        const errorElement = element.nextElementSibling;
        if (errorElement && errorElement.classList.contains('error-message')) {
            errorElement.textContent = '';
        }
    }
}

const validationRules = {
    required: (value) => value.trim() !== '',
    email: (value) => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value),
    phone: (value) => /^[0-9]{10,15}$/.test(value), // Adjust pattern as needed
    // Add more validation rules as necessary
};

const validationConfig = [
    {
        className: 'validate-required',
        rules: [
            { rule: 'required', message: 'This field is required' }
        ]
    },
    {
        className: 'validate-email',
        rules: [
            { rule: 'email', message: 'Invalid email format' }
        ]
    },
    {
        className: 'validate-phone',
        rules: [
            { rule: 'required', message: 'Phone number is required' },
            { rule: 'phone', message: 'Phone number must be 10-15 digits' }
        ]
    },
];

document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('patient-form');
    if (form) {
        new FormValidator(form, validationConfig);
    }
});


// const api = 'https://tonu.rocks/school/GreenHouse/api/';
const api = 'http://192.168.64.6/univ/PS1/public/api/preferences/';

var updatePreferences,
    openModal,
    nearest = (name, node) => {
        while (node.className.indexOf(name) == -1 && parent != null)
            node = node.parentElement;
        return node;
    };

async function getRecordsAsync(url) {
    let response = await fetch(url),
        data = await response.json()
    return data;
}

document.addEventListener('DOMContentLoaded', function () {

    const inputs = document.querySelectorAll('.input-field input[required]'),
        preferencesSelect = document.querySelector('select'),
        addIcon = (element) => {
            let parent = element.parentElement;
            parent.className = 'd_fl a_c j_sb';
            parent.innerHTML +=
                `<i class="material-icons clickable modal-trigger"
                onclick="openModal(event)">delete</i>`
        }

    const overloadSelect = () => {
        M.updateTextFields();
        M.FormSelect.init(preferencesSelect)

        const list = document.querySelectorAll(".dropdown-content li");
        list.forEach(item => {
            addIcon(item.firstElementChild)
            item.addEventListener("click", function () {
                let currentName = this.innerText.replace('\ndelete', ''),
                    origin = document.querySelector('[value="' + currentName + '"]'),
                    dataId = origin.getAttribute('data-id');

                getRecordsAsync(api + "?id=" + dataId)
                    .then(data => {
                        data.forEach(preference => {
                            console.log(preference)
                            inputs[0].value = preference.light;
                            inputs[1].value = preference.temperature;
                            inputs[2].value = preference.water;
                        })
                    });
            });
        });
    }

    getRecordsAsync(api)
        .then(response => {
            let selectedPreference = null;
            response.forEach(element => {

                // get selected preference
                if (element.selected) {
                    selectedPreference = element.current;
                    inputs[0].value = element.light;
                    inputs[1].value = element.temperature;
                    inputs[2].value = element.water;
                }
                preferencesSelect.innerHTML +=
                    `<option value="${element.name}"
                        ${element.selected ? 'selected' : ''}
                        data-id="${element.id}"
                        >${element.name}
                    </option>`

            });
            overloadSelect();
        });

    const preferenceForm = document.querySelector("#preferenceForm");
    preferenceForm.onsubmit = async function (event) {
        event.preventDefault();
        const formData = new FormData(this);
        const name = M.FormSelect.getInstance(preferencesSelect).getSelectedValues();
        const id = getOptionIdByName(name);
        console.log(name, id)
        if (formData.get("light") != '' ||
            formData.get("temperature") != '' ||
            formData.get("water") != ''
        ) {
            try {
                const response = await fetch(api, {
                    method: 'UPDATE',
                    body: JSON.stringify({
                        "id": id,
                        "light": formData.get("light"),
                        "temperature": formData.get("temperature"),
                        "water": formData.get("water")
                    })
                });
                const data = await response.text();
                console.log(data);


                M.toast({ html: 'Successfully updated', classes: 'indigo_toast' });
            } catch (err) {
                console.error(err);
                M.toast({ html: 'Something went wrong', classes: 'indigo_toast' });
            }
        }
        else {
            M.toast({ html: 'Fulfill all inputs!', classes: 'indigo_toast' });
            return false;
        }
    }

    const getOptionIdByName = (preference) => {
        const options = document.querySelectorAll("#kind option");
        let id = null;
        options.forEach(option => {
            if (option.innerHTML.trim() == preference)
                id = option.getAttribute("data-id");
        });
        return id;
    }

    const deletePreference = document.querySelector("#deletePreference"),
        deleteModal = document.querySelector("#deleteModal");

    openModal = (event) => {
        const trigger = event.target || event.srcElement,
            span = trigger.parentElement.querySelector("span"),
            preference = span.innerHTML.trim(),
            preferenceId = getOptionIdByName(preference);

        deletePreference.onclick = () => {
            if (preferenceId !== null) {
                fetch(api, {
                    method: 'DELETE',
                    body: JSON.stringify({
                        "id": preferenceId
                    })
                }).then(window.location.reload())
            }
        }
        M.Modal.getInstance(deleteModal).open()
    }

    var navElems = document.querySelectorAll('.sidenav'),
        navOptions = {
            "edge": "right"
        },
        modlas = document.querySelectorAll('.modal');
    M.Sidenav.init(navElems, navOptions);
    M.Modal.init(modlas);

});
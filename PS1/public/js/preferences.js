
// const api = 'https://tonu.rocks/school/GreenHouse/api/';
const api = 'http://192.168.64.6/univ/PS1/public/api/preferences/';

var updatePreferences,
    deletePreference,
    nearest = (name, node) => {
        while (node.className.indexOf(name) == -1 && parent != null)
            node = node.parentElement;
        return node;
    },
    updateCurrentAsync = (record) => {
        data = {
            "name": record.name,
            "selected": 1,
            "light": record.light,
            "temperature": record.temperature,
            "water": record.water
        }
        fetch(api + ':3000/preferences/0', {
            headers: { "Content-type": "application/json" },
            method: 'PATCH',
            body: JSON.stringify(data)
        })
            .then(response => response.json())
            .then(json => console.log(json))
    }

async function getRecordsAsync(url) {
    let response = await fetch(url),
        data = await response.json()
    return data;
}

document.addEventListener('DOMContentLoaded', function () {

    const inputs = document.querySelectorAll('.input-field input[required]'),
        preferencesSelect = document.querySelector('select');


    deletePreference = (id) => {
        fetch(api + ':3000/preferences/' + id, { method: 'DELETE' });
        location.reload();
        let el = event.currentTarget.parentElement,
            trigger = document.querySelector('.select-dropdown.dropdown-trigger');
        // el.parentElement.removeChild(el);

        // currentId = getRecordsAsync(api + ':3000/preferences/0')
        //     .then(data => {
        //         trigger.value = data.current;
        //     })

    };
    const addIcon = (element) => {
        let parent = element.parentElement;
        parent.className = 'd_fl a_c j_sb';
        parent.innerHTML += `<i class="material-icons clickable" onclick="deletePreference('${element?.id}')">delete</i>`
    }
    getRecordsAsync(api)
        .then(response => {
            let selectedPreference = null;
            response.forEach(element => {

                // get selected preference
                if (element.selected) {
                    console.log(element)
                    selectedPreference = element.current;
                    inputs[0].value = element.light;
                    inputs[1].value = element.temperature;
                    inputs[2].value = element.water;
                }
                preferencesSelect.innerHTML +=
                    `<option data-id="${element.id}" ${element.selected ? 'selected' : ''}
                    value="${element.name}">${element.name}</option>`

            });
            M.updateTextFields();
            M.FormSelect.init(preferencesSelect)
            let list = document.querySelectorAll(".dropdown-content span")
            list = document.querySelectorAll(".dropdown-content span");
            list.forEach(item => {
                addIcon(item)
                console.log(item)
                item.onclick = () => {
                    let currentName = this.innerText.replace('\ndelete', ''),
                        origin = document.querySelector('[value="' + currentName + '"]'),
                        dataId = origin.getAttribute('data-id');

                    currentId = getRecordsAsync(api + ':3000/preferences/' + dataId)
                        .then(data => {
                            updateCurrentAsync(data);
                            inputs[0].value = data.light;
                            inputs[1].value = data.temperature;
                            inputs[2].value = data.water;
                        });
                }
            })
        });

    updatePreferences = () => {
        currentId = getRecordsAsync(api + ':3000/preferences/0')
            .then(data => {
                var record = new Object();
                record = {
                    "light": Number(inputs[0].value),
                    "temperature": Number(inputs[1].value),
                    "water": Number(inputs[2].value)
                }
                fetch(api + ':3000/preferences/' + data.currentId, {
                    headers: { 'Content-Type': 'application/json' },
                    method: "PATCH",
                    body: JSON.stringify(record)
                })
            })
    }

    var navElems = document.querySelectorAll('.sidenav'),
        navOptions = {
            "edge": "right"
        };
    M.Sidenav.init(navElems, navOptions);

});
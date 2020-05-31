const API = "http://" + window.location.hostname;

var updatePreferences,
    likePost,
    deletePreference,
    nearest = (name, node) => {
        while (node.className.indexOf(name) == -1 && parent != null)
            node = node.parentElement;
        return node;
    },
    updateCurrentAsync = (record) => {
        data = {
            "current": record.name,
            "currentId": record.id,
            "light": record.light,
            "temperature": record.temperature,
            "water": record.water
        }
        fetch(API + ':3000/preferences/0', {
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

    var inputs = document.querySelectorAll('.input-field input[required]'),
        selElems = document.querySelectorAll('select');


    deletePreference = (id) => {
        fetch(API + ':3000/preferences/' + id, { method: 'DELETE' });
        location.reload();
        let el = event.currentTarget.parentElement,
            trigger = document.querySelector('.select-dropdown.dropdown-trigger');
        // el.parentElement.removeChild(el);

        // currentId = getRecordsAsync(API + ':3000/preferences/0')
        //     .then(data => {
        //         trigger.value = data.current;
        //     })

    };
    var addIcon = (element, item) => {
        let parent = item.parentElement;
        parent.className = 'd_fl a_c j_sb';
        parent.innerHTML += `<i class="material-icons clickable" onclick="deletePreference('${element.id}')">delete</i>`
    }
    getRecordsAsync(API + ":3000/preferences")
        .then(path => {
            path.forEach(element => {
                if (element.current != undefined) {
                    current = element.current;
                    return;
                }
                let selected = '';
                element.name == current ? selected = 'selected' : '';
                selElems[0].innerHTML += `
                        <option data-id="${element.id}" ${selected} value="${element.name}">${element.name}</option>
                    `
                if (element.name == current) {
                    inputs[0].value = element.light;
                    inputs[1].value = element.temperature;
                    inputs[2].value = element.water;
                }
            });
            M.updateTextFields();
            M.FormSelect.init(selElems)
            let list = document.querySelectorAll(".dropdown-content span")
            for (let i = 0; i < path.length - 1; i++) {
                addIcon(path[i + 1], list[i]);
            }
            list = document.querySelectorAll(".dropdown-content span");
            list.forEach(item => {
                item.addEventListener("click", function () {

                    let currentName = this.innerText.replace('\ndelete', ''),
                        origin = document.querySelector('[value="' + currentName + '"]'),
                        dataId = origin.getAttribute('data-id');

                    currentId = getRecordsAsync(API + ':3000/preferences/' + dataId)
                        .then(data => {
                            updateCurrentAsync(data);
                            inputs[0].value = data.light;
                            inputs[1].value = data.temperature;
                            inputs[2].value = data.water;
                        });

                });
            })
        });
    updatePreferences = () => {
        currentId = getRecordsAsync(API + ':3000/preferences/0')
            .then(data => {
                var record = new Object();
                record = {
                    "light": Number(inputs[0].value),
                    "temperature": Number(inputs[1].value),
                    "water": Number(inputs[2].value)
                }
                fetch(API + ':3000/preferences/' + data.currentId, {
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
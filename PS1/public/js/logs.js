var makeLog, likeLog, deleteLog, deleteLog, setFilter,
    nearest = (name, node) => {
        while (node.className.indexOf(name) == -1 && parent != null)
            node = node.parentElement;
        return node;
    }

document.addEventListener('DOMContentLoaded', function () {
    const form = document.querySelector("form"),
        logs_container = document.getElementById("logs"),
        api = 'https://tonu.rocks/school/GreenHouse/api/logs/';
    async function getRecordsAsync(url) {
        let response = await fetch(url),
            data = await response.json()
        return data;
    }
    const months = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
    const current_datetime = new Date();
    const formatted_date = months[current_datetime.getMonth()] + ' ' + current_datetime.getDate() + ", " + current_datetime.getFullYear();
    // getRecordsAsync(api + '/light.php/?q='+formatted_date+'&?_sort=time&_order=asc&_page=1&_limit=10')
    //     .then(data => {
    //         console.log(formatted_date);
    //         renderLogs(data)
    //     })
    getRecordsAsync(api)
        .then(data => {
            console.log(formatted_date);
            renderLogs(data)
        })
    var renderLogs = (records) => {
        console.log(records);
        logs_container.innerHTML = `
            <table class="striped centered">
                <thead>
                    <tr>
                        <th class="date_cell">Date</th>
                        <th>Time</th>
                        <th>Value</th>
                        <th>Setting</th>
                        <th width="60">Action</th>
                    </tr>
                </thead>
                <tbody>`;
        records.forEach(element => {
            logs_container.querySelector('tbody').innerHTML += `
                    <tr class="log">
                        <td class="date_cell">${element.timestamp.split(" ")[0]}</td>
                        <td>${element.timestamp.split(" ")[1]}</td>
                        <td>${element.lightValue}</td>
                        <td>${element.lightSet}</td>
                        <td><i class="material-icons clickable" onclick="deleteLog('${element.id}')">delete</i><td>
                    </tr>`
        });
    }
    deleteLog = (id) => {
        fetch(api + '/' + getActiveTab() + '/' + id, { method: 'DELETE' })
        logs_container.querySelector('tbody').removeChild(nearest("log", event.target));
    }
    setFilter = () => {
        let date = this.querySelector(".datepicker").value,
            time = this.querySelector(".timepicker").value;
        if (date != "" && time != "") {
            updateLogs(getActiveTab().toLowerCase(), date, time + ":00")
        }
        else {
            M.toast({ html: 'Please select date and time' })
        }
    }
    var getActiveTab = () => {
        var tab = document.querySelector('.tabs .active');
        return tab.innerHTML;
    },
        updateLogs = (type, date, time) => {
            let location = api + `?type=${type}&date=${date}&time_gte=${time}`;
            getRecordsAsync(location)
                .then(data => {
                    renderLogs(data)
                    console.log(type, data, logs_container, location)
                })
        }
    navElems = document.querySelectorAll('.sidenav'),
        tabElems = document.querySelectorAll('.tabs'),
        navOptions = {
            "edge": "right"
        },
        tabOptions = {
            "onShow": function () {
                var propery = this.$activeTabLink[0].hash.replace('#', '');
                getRecordsAsync(`${api}?propery=${propery}`)
                    .then(data => renderLogs(data));
            }
        },
        datepicker = document.querySelectorAll('.datepicker'),
        timepicker = document.querySelectorAll('.timepicker'),
        currentDate = new Date(),
        currentYear = currentDate.getFullYear(),
        dateOptions = {
            "maxDate": currentDate,
            "yearRange": [currentYear - 5, currentYear]
        }
    timeOptions = {
        "twelveHour": false
    }

    M.Sidenav.init(navElems, navOptions);
    M.Datepicker.init(datepicker, dateOptions);
    M.Timepicker.init(timepicker, timeOptions);
    M.Tabs.init(tabElems, tabOptions);
});
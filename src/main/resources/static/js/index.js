document.addEventListener('DOMContentLoaded', function() {
    mostrarCargando(true);
    cargarDoctores();
    cargarConsultorios();
    cargarCitas();
    mostrarCargando(false);
});

function mostrarCargando(mostrar) {
    const loadingOverlay = document.getElementById('loadingOverlay');
    if (loadingOverlay) {
        loadingOverlay.style.display = mostrar ? 'flex' : 'none';
    }
}

function cargarDoctores() {
    fetch('/api/doctores')
        .then(response => response.json())
        .then(doctores => {
            let content = '<ul class="list-group">';
            doctores.forEach(doc => {
                content += `<li class="list-group-item">${doc.nombre} ${doc.apellidoPaterno} - ${doc.especialidad}</li>`;
            });
            content += '</ul>';
            document.getElementById('doctoresDisplay').innerHTML = content;
        }).catch(error => alert('Error al cargar doctores: ' + error));
}

function cargarConsultorios() {
    fetch('/api/consultorios')
        .then(response => response.json())
        .then(consultorios => {
            let content = '<ul class="list-group">';
            consultorios.forEach(con => {
                content += `<li class="list-group-item">Consultorio ${con.numeroConsultorio} - Piso ${con.piso}</li>`;
            });
            content += '</ul>';
            document.getElementById('consultoriosDisplay').innerHTML = content;
        }).catch(error => alert('Error al cargar consultorio: ' + error));
}

function cargarCitas() {
    fetch('/api/citas')
        .then(response => response.json())
        .then(citas => {
            let content = '<ul class="list-group">';
            citas.forEach(cita => {
                content += `<li class="list-group-item">${cita.nombrePaciente} - ${cita.horarioConsulta}</li>`;
            });
            content += '</ul>';
            document.getElementById('citasDisplay').innerHTML = content;
        }).catch(error => alert('Error al cargar cita: ' + error));
}


function mostrarModalDoctor() {
    const modal = new bootstrap.Modal(document.getElementById('modalDoctor'));
    modal.show();
}

function mostrarModalConsultorio() {
    const modal = new bootstrap.Modal(document.getElementById('modalConsultorio'));
    modal.show();
}

function mostrarModalCita() {
    mostrarCargando(true);
    cargarDoctoresEnSelect();
    cargarConsultoriosEnSelect();
    const modal = new bootstrap.Modal(document.getElementById('modalCita'));
    modal.show();
    mostrarCargando(false);
}

function cargarDoctoresEnSelect() {
    fetch('/api/doctores')
        .then(response => response.json())
        .then(doctores => {
            const doctorSelect = document.getElementById('doctorCita');
            doctorSelect.innerHTML = '<option value="">Selecciona un doctor</option>';
            doctores.forEach(doctor => {
                const option = document.createElement('option');
                option.value = doctor.id;
                option.textContent = `${doctor.nombre} ${doctor.apellidoPaterno}`;
                doctorSelect.appendChild(option);
            });
        }).catch(error => alert('Error al cargar doctores: ' + error));
}

function cargarConsultoriosEnSelect() {
    fetch('/api/consultorios')
        .then(response => response.json())
        .then(consultorios => {
            const consultorioSelect = document.getElementById('consultorioCita');
            consultorioSelect.innerHTML = '<option value="">Selecciona un consultorio</option>';
            consultorios.forEach(consultorio => {
                const option = document.createElement('option');
                option.value = consultorio.id;
                option.textContent = `Consultorio ${consultorio.numeroConsultorio} - Piso ${consultorio.piso}`;
                consultorioSelect.appendChild(option);
            });
        }).catch(error => alert('Error al cargar consultorios: ' + error));
}

function cerrarModal(modalId) {
    location.reload();
}

function guardarDoctor() {
    mostrarCargando(true)
    const doctor = {
        nombre: document.getElementById('nombreDoctor').value,
        apellidoPaterno: document.getElementById('apellidoPaterno').value,
        apellidoMaterno: document.getElementById('apellidoMaterno').value,
        especialidad: document.getElementById('especialidad').value
    };

    fetch('/api/doctores', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(doctor)
    }).then(() => {
        cargarDoctores();
        cerrarModal('modalDoctor');
    }).catch(error => alert('Error al registrar doctor: ' + error));

    mostrarCargando(false)
}

function guardarConsultorio() {
    mostrarCargando(true)
    const consultorio = {
        numeroConsultorio: document.getElementById('numeroConsultorio').value,
        piso: document.getElementById('pisoConsultorio').value
    };

    fetch('/api/consultorios', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(consultorio)
    }).then(() => {
        cargarConsultorios();
        cerrarModal('modalConsultorio');
    }).catch(error => alert('Error registrar consultorio: ' + error));
    mostrarCargando(false)
}

function guardarCita() {
    mostrarCargando(true);
    const nombrePaciente = document.getElementById('nombrePaciente').value.trim();
    const horarioConsulta = document.getElementById('horarioConsulta').value;
    const doctorId = document.getElementById('doctorCita').value;
    const consultorioId = document.getElementById('consultorioCita').value;

    if (!nombrePaciente || !horarioConsulta || !doctorId || !consultorioId) {
        alert('Por favor, completa todos los campos.');
        mostrarCargando(false);
        return;
    }

    const cita = {
        nombrePaciente: nombrePaciente,
        horarioConsulta: horarioConsulta,
        doctor: { id: doctorId },
        consultorio: { id: consultorioId }
    };

    fetch('/api/citas', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(cita)
    })
        .then(response => {
            if (response.ok) {
                cargarCitas();
                cerrarModal('modalCita');
            } else {
                return response.text().then(errorText => {
                    try {
                        const errorData = JSON.parse(errorText);
                        if (errorData.message) {
                            alert('Error: ' + errorData.message);
                        } else {
                            alert('Error al guardar la cita.');
                        }
                    } catch {
                        alert('Error: ' + errorText);
                    }
                });
            }
        })
        .catch(error => alert('Error al guardar la cita: ' + error))
        .finally(() => mostrarCargando(false));
}


document.addEventListener("DOMContentLoaded", function() {
    cargarDoctores();
    cargarConsultorios();
    cargarCitas();
});

function cargarDoctores() {
    fetch('/api/doctores').then(response => response.json()).then(doctores => {
        let content = '<ul class="list-group">';
        doctores.forEach(doc => {
            content += `<li class="list-group-item">${doc.nombre} ${doc.apellidoPaterno} - ${doc.especialidad}</li>`;
        });
        content += '</ul>';
        document.getElementById('doctoresDisplay').innerHTML = content;
    }).catch(error => alert('Error al cargar doctores: ' + error));
}

function cargarConsultorios() {
    fetch('/api/consultorios').then(response => response.json()).then(consultorios => {
        let content = '<ul class="list-group">';
        consultorios.forEach(con => {
            content += `<li class="list-group-item">Consultorio ${con.numeroConsultorio} - Piso ${con.piso}</li>`;
        });
        content += '</ul>';
        document.getElementById('consultoriosDisplay').innerHTML = content;
    }).catch(error => alert('Error al cargar consultorios: ' + error));
}

function cargarCitas() {
    fetch('/api/citas').then(response => response.json()).then(citas => {
        let content = '<ul class="list-group">';
        citas.forEach(cita => {
            content += `<li class="list-group-item">${cita.nombrePaciente} - ${cita.horarioConsulta}</li>`;
        });
        content += '</ul>';
        document.getElementById('citasDisplay').innerHTML = content;
    }).catch(error => alert('Error al cargar citas: ' + error));
}


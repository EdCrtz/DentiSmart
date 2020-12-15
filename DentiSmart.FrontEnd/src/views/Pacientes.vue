<template>
  <div class="container">
    <v-data-table
      :headers="headers"
      :items="pacientes"
      sort-by="calories"
      class="elevation-1"
      :search="search"
    >
      <template v-slot:[`item.fullName`]="{ item }">
        {{ `${item.nombre} ${item.apellidoPat} ${item.apellidoMat}` }}
      </template>

      <template v-slot:top>
        <v-toolbar color="azulSecundario" dark flat>
          <v-toolbar-title>Pacientes</v-toolbar-title>

          <v-spacer></v-spacer>
          <v-row>
            <v-col :cols="$vuetify.breakpoint.mobile ? 10 : 12">
              <v-text-field
                v-model="search"
                append-icon="mdi-magnify"
                label="Buscar Paciente..."
                single-line
                hide-details
              ></v-text-field>
            </v-col>
          </v-row>

          <v-spacer></v-spacer>
          <v-dialog v-model="dialog" persistent max-width="500px">
            <template v-slot:activator="{ on, attrs }">
              <v-btn
                fab
                dark
                small
                absolute
                bottom
                right
                color="#0288d1"
                class="mb-2"
                v-bind="attrs"
                v-on="on"
              >
                <v-icon>mdi-plus</v-icon>
              </v-btn>
            </template>
            <v-card>
              <v-toolbar color="azulSecundario" dark flat>
                <span class="headline">{{ formTitle }}</span>
                <v-spacer></v-spacer>
                <v-btn icon @click="close">
                  <v-icon>mdi-close</v-icon>
                </v-btn>
              </v-toolbar>

              <v-card-text>
                <v-container>
                  <v-form ref="form">
                    <v-row>
                      <v-col cols="12">
                        <v-text-field
                          v-model="editedItem.nombre"
                          label="Nombre"
                          :rules="rules.nombre"
                          required
                        ></v-text-field>
                      </v-col>
                      <v-col cols="12" sm="6">
                        <v-text-field
                          v-model="editedItem.apellidoPat"
                          label="Apellido Paterno"
                          :rules="rules.apellidoPat"
                          required
                        ></v-text-field>
                      </v-col>
                      <v-col cols="12" sm="6">
                        <v-text-field
                          v-model="editedItem.apellidoMat"
                          label="Apellido Materno"
                          :rules="rules.apellidoMat"
                        ></v-text-field>
                      </v-col>
                      <v-col cols="12" sm="6">
                        <v-text-field
                          v-model="editedItem.telefono"
                          label="Numero de telefono"
                          :rules="rules.telefono"
                          required
                        ></v-text-field>
                      </v-col>
                      <v-col cols="12" sm="6">
                        <v-text-field
                          v-model="editedItem.email"
                          label="Correo Electronico"
                          :rules="rules.email"
                        ></v-text-field>
                      </v-col>

                      <v-col cols="12">
                        <v-text-field
                          v-model="editedItem.nombreUsuario"
                          label="Nombre de usuario"
                          :rules="rules.nombreUsuario"
                          required
                        ></v-text-field>
                      </v-col>

                      <v-col cols="12">
                        <v-text-field
                          v-model="editedItem.contrasenia"
                          label="Contraseña"
                          :rules="rules.contrasenia"
                          required
                          type="password"
                        ></v-text-field>
                      </v-col>
                    </v-row>
                  </v-form>
                </v-container>
              </v-card-text>

              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn block color="blue darken-1" text @click="save">
                  Guardar
                </v-btn>
                <v-spacer></v-spacer>
              </v-card-actions>
            </v-card>
          </v-dialog>

          <v-dialog
            v-model="dialogEditarExpediente"
            persistent
            max-width="670px"
          >
            <v-card>
              <v-toolbar color="azulSecundario" dark flat>
                <span class="headline"> Editar Expediente</span>
                <v-spacer></v-spacer>
                <v-btn icon @click="close">
                  <v-icon>mdi-close</v-icon>
                </v-btn>
              </v-toolbar>

              <v-card-actions>
                <v-spacer></v-spacer>
                <v-card-text>
                  <v-container>
                    <v-form ref="form">
                      <h3 align="center" justify="center">
                        Antecedentes Personales Patológicos
                      </h3>
                      <div v-if="editedItem.expediente != null">
                        <v-row>
                          <v-col
                            cols="12"
                            style="display: flex; margin-right: 20px"
                            v-for="(obj, i) in objeto"
                            v-bind:key="i"
                          >
                            <v-radio-group
                              v-model="obj.estatus"
                              row
                              @change="cambiosRadio(obj.nombre, obj.estatus)"
                              style="width: 65%"
                            >
                              <h3
                                v-if="
                                  obtenerEnfermedad(obj.nombre) !=
                                    'Cardiovasculares' &&
                                  obtenerEnfermedad(obj.nombre) !=
                                    'Transfuciones'
                                "
                                style="margin-right: 20px; width: 200px"
                              >
                                {{ obtenerEnfermedad(obj.nombre) }}
                              </h3>

                              <h3
                                v-if="
                                  obtenerEnfermedad(obj.nombre) ==
                                  'Cardiovasculares'
                                "
                                style="margin-right: 20px; width: 200px"
                              >
                                Enfermedades {{ obtenerEnfermedad(obj.nombre) }}
                              </h3>

                              <h3
                                v-if="
                                  obtenerEnfermedad(obj.nombre) ==
                                  'Transfuciones'
                                "
                                style="margin-right: 20px; width: 200px"
                              >
                                {{ obtenerEnfermedad(obj.nombre) }} sanguíneas
                              </h3>
                              <v-radio label="SI" value="SI"></v-radio>
                              <v-radio label="NO" value="NO"></v-radio>
                            </v-radio-group>
                            <v-combobox
                              :items="years"
                              style="width: 35%"
                              v-model="obj.año"
                              label="Año diagnosticado"
                              :disabled="disabledfechas[obj.nombre]"
                              @change="actualizarFecha(obj.nombre, obj.año)"
                              small-chips
                            ></v-combobox>
                          </v-col>
                        </v-row>
                        <v-row>
                          <v-col
                            cols="12"
                            style="
                              position: relative;
                              display: flex;
                              margin-right: 20px;
                            "
                          >
                            <v-combobox
                              v-model="alergia"
                              label="Alergias a:"
                              multiple
                              chips
                              clearable
                            ></v-combobox>

                            <!--  <v-btn @click="agregarAlergia(alergia)" color="primary" style="height: 50%; position: absolute; right: -10px; border-top-right-radius: 25px;border-bottom-right-radius: 25px;">
                            Añadir
                            </v-btn> -->
                          </v-col>

                          <v-col
                            cols="12"
                            style="
                              position: relative;
                              display: flex;
                              margin-right: 20px;
                            "
                          >
                            <v-text-field
                              v-model="editedItem.expediente.descripcionOtro"
                              label="Otro"
                            >
                            </v-text-field>
                          </v-col>
                        </v-row>
                      </div>
                    </v-form>
                  </v-container>
                </v-card-text>
              </v-card-actions>
              <v-spacer></v-spacer>
              <v-card-actions>
                <v-btn
                  block
                  color="blue darken-1"
                  text
                  @click="actualizarExpediente()"
                >
                  Actualizar Expediente
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>
          <v-dialog v-model="dialogExpediente" persistent max-width="670px">
            <v-card>
              <v-toolbar color="azulSecundario" dark flat>
                <span class="headline"> Crear Expediente</span>
                <v-spacer></v-spacer>
                <v-btn icon @click="close">
                  <v-icon>mdi-close</v-icon>
                </v-btn>
              </v-toolbar>

              <v-card-actions>
                <v-spacer></v-spacer>
                <v-card-text>
                  <v-container>
                    <v-form ref="form">
                      <h3 align="center" justify="center">
                        Antecedentes Personales Patológicos
                      </h3>

                      <v-row>
                        <v-col
                          cols="12"
                          style="display: flex; margin-right: 20px"
                        >
                          <v-radio-group
                            v-model="row.varicela"
                            row
                            @change="cambiosRadio('varicela', row.varicela)"
                            style="width: 65%"
                          >
                            <h3 style="margin-right: 20px; width: 200px">
                              Varicela:
                            </h3>
                            <v-radio label="SI" value="SI"></v-radio>
                            <v-radio label="NO" value="NO"></v-radio>
                          </v-radio-group>
                          <v-combobox
                            :items="years"
                            style="width: 35%"
                            v-model="fechas.varicela"
                            label="Año diagnosticado"
                            :disabled="disabledfechas.varicela"
                            small-chips
                          ></v-combobox>
                        </v-col>
                        <v-col
                          cols="12"
                          style="display: flex; margin-right: 20px"
                        >
                          <v-radio-group
                            v-model="row.sarampion"
                            row
                            @change="cambiosRadio('sarampion', row.sarampion)"
                            style="width: 80%"
                          >
                            <h3 style="margin-right: 20px; width: 200px">
                              Sarampión:
                            </h3>
                            <v-radio label="SI" value="SI"></v-radio>
                            <v-radio label="NO" value="NO"></v-radio>
                          </v-radio-group>
                          <v-combobox
                            :items="years"
                            style="width: 200px"
                            v-model="fechas.sarampion"
                            :disabled="disabledfechas.sarampion"
                            label="Año diagnosticado"
                            clearable
                            small-chips
                          ></v-combobox>
                        </v-col>
                        <v-col
                          cols="12"
                          style="display: flex; margin-right: 20px"
                        >
                          <v-radio-group
                            v-model="row.asma"
                            row
                            @change="cambiosRadio('asma', row.asma)"
                            style="width: 80%"
                          >
                            <h3 style="margin-right: 20px; width: 200px">
                              Asma:
                            </h3>
                            <v-radio label="SI" value="SI"></v-radio>
                            <v-radio label="NO" value="NO"></v-radio>
                          </v-radio-group>
                          <v-combobox
                            :items="years"
                            style="width: 200px"
                            label="Año diagnosticado"
                            v-model="fechas.asma"
                            clearable
                            :disabled="disabledfechas.asma"
                            small-chips
                          ></v-combobox>
                        </v-col>
                        <v-col
                          cols="12"
                          style="display: flex; margin-right: 20px"
                        >
                          <v-radio-group
                            v-model="row.diabetes"
                            row
                            @change="cambiosRadio('diabetes', row.diabetes)"
                            style="width: 80%"
                          >
                            <h3 style="margin-right: 20px; width: 200px">
                              Diabetes:
                            </h3>
                            <v-radio label="SI" value="SI"></v-radio>
                            <v-radio label="NO" value="NO"></v-radio>
                          </v-radio-group>
                          <v-combobox
                            :items="years"
                            style="width: 200px"
                            label="Año diagnosticado"
                            v-model="fechas.diabetes"
                            :disabled="disabledfechas.diabetes"
                            clearable
                            small-chips
                          ></v-combobox>
                        </v-col>
                        <v-col
                          cols="12"
                          style="display: flex; margin-right: 20px"
                        >
                          <v-radio-group
                            v-model="row.cardiovasculares"
                            row
                            @change="
                              cambiosRadio(
                                'cardiovasculares',
                                row.cardiovasculares
                              )
                            "
                            style="width: 80%"
                          >
                            <h3 style="margin-right: 20px; width: 200px">
                              Enf. Cardiovasculares:
                            </h3>
                            <v-radio label="SI" value="SI"></v-radio>
                            <v-radio label="NO" value="NO"></v-radio>
                          </v-radio-group>
                          <v-combobox
                            :items="years"
                            style="width: 200px"
                            label="Año diagnosticado"
                            :disabled="disabledfechas.cardiovasculares"
                            v-model="fechas.cardiovasculares"
                            clearable
                            small-chips
                          ></v-combobox>
                        </v-col>
                        <v-col
                          cols="12"
                          style="display: flex; margin-right: 20px"
                        >
                          <v-radio-group
                            v-model="row.transfuciones"
                            row
                            @change="
                              cambiosRadio('transfuciones', row.transfuciones)
                            "
                            style="width: 80%"
                          >
                            <h3 style="margin-right: 20px; width: 200px">
                              Transfuciones sanguineas:
                            </h3>
                            <v-radio label="SI" value="SI"></v-radio>
                            <v-radio label="NO" value="NO"></v-radio>
                          </v-radio-group>
                          <v-combobox
                            :items="years"
                            style="width: 200px"
                            label="Año diagnosticado"
                            v-model="fechas.transfuciones"
                            :disabled="disabledfechas.transfuciones"
                            clearable
                            small-chips
                          ></v-combobox>
                        </v-col>
                      </v-row>
                      <v-row>
                        <v-col
                          cols="12"
                          style="
                            position: relative;
                            display: flex;
                            margin-right: 20px;
                          "
                        >
                          <v-combobox
                            v-model="alergia"
                            label="Alergias a:"
                            multiple
                            chips
                            clearable
                          ></v-combobox>

                          <!--  <v-btn @click="agregarAlergia(alergia)" color="primary" style="height: 50%; position: absolute; right: -10px; border-top-right-radius: 25px;border-bottom-right-radius: 25px;">
                            Añadir
                            </v-btn> -->
                        </v-col>

                        <v-col
                          cols="12"
                          style="
                            position: relative;
                            display: flex;
                            margin-right: 20px;
                          "
                        >
                          <v-text-field v-model="otraDescripcion" label="Otro">
                          </v-text-field>
                        </v-col>
                      </v-row>
                    </v-form>
                  </v-container>
                </v-card-text>
              </v-card-actions>
              <v-spacer></v-spacer>
              <v-card-actions>
                <v-btn
                  block
                  color="blue darken-1"
                  text
                  @click="generarExpediente()"
                >
                  Generar Expediente
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>
          <v-dialog v-model="dialogDelete" max-width="500px">
            <v-card>
              <v-card-title class="headline"
                >Estas seguro que quieres eliminar a este
                paciente?</v-card-title
              >
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="blue darken-1" text @click="closeDelete"
                  >Cancelar</v-btn
                >
                <v-btn color="blue darken-1" text @click="deleteItemConfirm"
                  >Borrar</v-btn
                >
                <v-spacer></v-spacer>
              </v-card-actions>
            </v-card>
          </v-dialog>
        </v-toolbar>
      </template>
      <template v-slot:[`item.actions`]="{ item }">
        <v-icon small class="mr-2" @click="crearExpediente(item)">
          mdi-card-account-details-outline
        </v-icon>
        <v-icon small class="mr-2" @click="editItem(item)"> mdi-pencil </v-icon>
        <v-icon small @click="deleteItem(item)"> mdi-delete </v-icon>
      </template>
      <template v-slot:no-data>
        No hay registros de usuarios tipo pacientes
      </template>
    </v-data-table>
  </div>
</template>

<script>
import axios from "axios";
import { environment } from "../env/environment";
export default {
  data: () => ({
    url: environment.url + "Usuario",
    dialog: false,
    dialogExpediente: false,
    dialogDelete: false,
    dialogEditarExpediente: false,
    otraDescripcion: null,
    objeto: null,
    objetoAlergias: null,
    enfermedadesAux: [],
    alergia: null,
    row: {
      varicela: null,
      sarampion: null,
      asma: null,
      diabetes: null,
      cardiovasculares: null,
      transfuciones: null,
    },
    fechas: {
      varicela: null,
      sarampion: null,
      asma: null,
      diabetes: null,
      cardiovasculares: null,
      transfuciones: null,
    },
    disabledfechas: {
      varicela: true,
      sarampion: true,
      asma: true,
      diabetes: true,
      cardiovasculares: true,
      transfuciones: true,
    },
    years: [],
    headers: [
      { text: "Nombre Completo", value: "fullName", sortable: true },
      { text: "Nombre de usuario", value: "nombreUsuario" },
      { text: "Numero de teléfono", value: "telefono" },
      { text: "Correo electrónico", value: "email" },
      { text: "Acciones", value: "actions", sortable: false },
    ],
    pacientes: [],

    search: "",
    editedIndex: -1,
    editedItem: {
      id: "",
      nombre: "",
      apellidoPat: "",
      apellidoMat: "",
      nombreUsuario: "",
      contrasenia: "",
      email: "",
      role: "",
      telefono: "",
      foto: "",
    },
    defaultItem: {
      id: "",
      nombre: "",
      apellidoPat: "",
      apellidoMat: "",
      nombreUsuario: "",
      contrasenia: "",
      email: "",
      role: "",
      telefono: "",
      foto: "",
      idFacebook: "",
      idGoogle: "",
    },
    rules: {
      nombre: [(val) => (val || "").length > 0 || "Este campo es requerido"],
      apellidoPat: [
        (val) => (val || "").length > 0 || "Este campo es requerido",
      ],
      telefono: [
        (val) => (val || "").length > 0 || "El telefono es requerido",
        (val) =>
          (val || "").length == 10 ||
          "El telefono solo debe contener 10 caracteres",
      ],
      nombreUsuario: [
        (val) => (val || "").length > 0 || "Nombre de usuario es requerido",
      ],
      contrasenia: [
        (val) => (val || "").length > 0 || "La contraseña es requerida",
      ],
    },
  }),

  computed: {
    formTitle() {
      return this.editedIndex === -1 ? "Nuevo paciente" : "Editar paciente";
    },
  },

  watch: {
    dialog(val) {
      if (!val) {
        this.close();
        this.$refs.form.reset();
      }
    },
    dialogDelete(val) {
      val || this.closeDelete();
    },
  },

  created() {
    this.initialize();
  },

  methods: {
    obtenerPacientes() {
      this.$vloading.show();
      axios
        .get(this.url + "/role/Paciente")
        .then((res) => {
          this.pacientes = res.data;
          this.$vloading.hide();
          console.log(res.data);
        })
        .catch((e) => {
          console.log(e);
          this.$vloading.hide();
          this.$toast.error("No se pudieron obtener los pacientes");
        });
    },

    agregarPaciente(paciente) {
      console.log(paciente);
      this.$vloading.show();
      axios
        .post(this.url, paciente)
        .then(() => {
          this.$vloading.hide();
          this.$toast.success("Se agrego el paciente correctamente");
          this.obtenerPacientes();
        })
        .catch((e) => {
          console.log(e);
          this.$vloading.hide();
          this.$toast.error("Error: No se pudo agregar al paciente ");
        });
    },
    editarPaciente(paciente) {
      console.log(paciente);
      axios
        .put(this.url, paciente)
        .then(() => {
          this.$vloading.hide();
          this.$toast.success(
            "Se actualizaron los datos del paciente correctamente"
          );
          this.obtenerPacientes();
        })
        .catch((e) => {
          console.log(e);
          this.$vloading.hide();
          this.$toast.error("No se pudieron actualizar los datos del paciente");
        });
    },
    eliminarPaciente(id) {
      this.$vloading.show();
      axios
        .delete(`${this.url}/${id}`)
        .then(() => {
          this.$vloading.hide();
          this.$toast.success("Se elimino al paciente correctamente");
          this.obtenerPacientes();
        })
        .catch((e) => {
          console.log(e);
          this.$vloading.hide();
          this.$toast.error("No se pudo eliminar al paciente ");
        });
    },
    initialize() {
      console.log(this.editedIndex);
      var aux = new Date();
      for (let i = 1950; i <= aux.getFullYear(); i++) {
        this.years.push(i);
      }
      this.years.push("Nulo");
      this.years.reverse();
      this.obtenerPacientes();
    },

    editItem(item) {
      this.editedIndex = this.pacientes.indexOf(item);
      this.editedItem = Object.assign({}, item);
      this.dialog = true;
    },

    crearExpediente(item) {
      this.reiniciarValores();
      this.editedIndex = this.pacientes.indexOf(item);
      this.editedItem = Object.assign({}, item);
      console.log(this.editedItem);

      if (this.editedItem.expediente == null) this.dialogExpediente = true;
      else {
        this.objeto = this.editedItem.expediente.antecedentes;
        this.alergia = this.editedItem.expediente.alergias;

        console.log(this.objeto);

        this.objeto.forEach((uwu) => {
          if (uwu.estatus == true || uwu.estatus == "SI") {
            uwu.estatus = "SI";
            this.row[uwu.nombre] = "SI";
            this.fechas[uwu.nombre] = uwu.año;
            this.disabledfechas[uwu.nombre] = false;
          } else {
            uwu.estatus = "NO";
            this.row[uwu.nombre] = "NO";
            this.disabledfechas[uwu.nombre] = true;
            this.fechas[uwu.nombre] = null;
          }
        });

      console.log(this.fechas);

        console.log(this.disabledfechas);
        /* 
      row: {
      varicela: null,
      sarampion: null,
      asma: null,
      diabetes: null,
      cardiovasculares: null,
      transfuciones: null,
    }, */

        this.dialogEditarExpediente = true;
      }
    },

    deleteItem(item) {
      this.editedIndex = this.pacientes.indexOf(item);
      this.editedItem = Object.assign({}, item);
      this.dialogDelete = true;
    },

    deleteItemConfirm() {
      this.pacientes.splice(this.editedIndex, 1);
      this.eliminarPaciente(this.editedItem.id);
      this.closeDelete();
    },

    close() {
      this.dialog = false;

      this.dialogEditarExpediente = false;
      this.dialogExpediente = false;
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem);
        this.editedIndex = -1;
      });
    },

    closeDelete() {
      this.dialogDelete = false;
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem);
        this.editedIndex = -1;
      });
    },

    save() {
      if (this.$refs.form.validate()) {
        const paciente = {
          id: this.editedItem.id,
          nombre: this.editedItem.nombre,
          apellidoPat: this.editedItem.apellidoPat,
          apellidoMat: this.editedItem.apellidoMat,
          telefono: this.editedItem.telefono,
          email: this.editedItem.email,
          nombreUsuario: this.editedItem.nombreUsuario,
          role: "Paciente",
          contrasenia: this.editedItem.contrasenia,
          foto: this.editedItem.foto,
          idFacebook: this.editedItem.idFacebook,
          idGoogle: this.editedItem.idGoogle,
        };

        if (this.editedIndex > -1) {
          console.log("se actualizo el paciente");
          this.editarPaciente(paciente);
        } else {
          this.agregarPaciente(paciente);
        }
        this.close();
      }
    },
    generarExpediente() {
      if (this.row.varicela == "SI") this.row.varicela = true;
      else this.row.varicela = false;
      if (this.row.sarampion == "SI") this.row.sarampion = true;
      else this.row.sarampion = false;
      if (this.row.asma == "SI") this.row.asma = true;
      else this.row.asma = false;
      if (this.row.diabetes == "SI") this.row.diabetes = true;
      else this.row.diabetes = false;
      if (this.row.cardiovasculares == "SI") this.row.cardiovasculares = true;
      else this.row.cardiovasculares = false;
      if (this.row.transfuciones == "SI") this.row.transfuciones = true;
      else this.row.transfuciones = false;

      this.enfermedadesAux = [];

      var enfermedades = [];
      enfermedades.push("varicela");
      enfermedades.push("sarampion");
      enfermedades.push("asma");
      enfermedades.push("diabetes");
      enfermedades.push("cardiovasculares");
      enfermedades.push("transfuciones");

      for (let i = 0; i < enfermedades.length; i++) {
        var bandera = enfermedades[i];
        var ayuda = {
          nombre: enfermedades[i],
          estatus: this.row[bandera],
          año: this.fechas[bandera],
        };
        this.enfermedadesAux.push(ayuda);
      }

      console.log(ayuda);
      var aux = {
        Antecedentes: this.enfermedadesAux,
        Alergias: this.alergia,
        DescripcionOtro: this.otraDescripcion,
      };

      const paciente = {
        id: this.editedItem.id,
        nombre: this.editedItem.nombre,
        apellidoPat: this.editedItem.apellidoPat,
        apellidoMat: this.editedItem.apellidoMat,
        telefono: this.editedItem.telefono,
        email: this.editedItem.email,
        nombreUsuario: this.editedItem.nombreUsuario,
        role: "Paciente",
        contrasenia: this.editedItem.contrasenia,
        foto: this.editedItem.foto,
        expediente: aux,
        idFacebook: this.editedItem.idFacebook,
        idGoogle: this.editedItem.idGoogle,
      };
      console.log(aux);

      this.$vloading.show();

      axios
        .put(this.url, paciente)
        .then(() => {
          this.$vloading.hide();
          this.$toast.success(
            "Se actualizaron los datos del paciente correctamente"
          );
          this.obtenerPacientes();
          this.close();

        })
        .catch((e) => {
          console.log(e);
          this.$vloading.hide();
          this.$toast.error("No se pudieron actualizar los datos del paciente");
        });
    },
    actualizarExpediente() {
      var enfermedades2 = [];
      enfermedades2.push("varicela");
      enfermedades2.push("sarampion");
      enfermedades2.push("asma");
      enfermedades2.push("diabetes");
      enfermedades2.push("cardiovasculares");
      enfermedades2.push("transfuciones");

      var banderin = false;
      enfermedades2.forEach((uwu) => {
        if ((this.row[uwu] == true || this.row[uwu] == "SI") && this.fechas[uwu] == null) {
          banderin = true;
          console.log(this.row[uwu]);
          console.log(this.fechas[uwu]);
        }
      });

      if (banderin) {
        this.$toast.error("Faltan datos por llenar");
        return;
      }

      if (this.row.varicela == "SI") this.row.varicela = true;
      else this.row.varicela = false;
      if (this.row.sarampion == "SI") this.row.sarampion = true;
      else this.row.sarampion = false;
      if (this.row.asma == "SI") this.row.asma = true;
      else this.row.asma = false;
      if (this.row.diabetes == "SI") this.row.diabetes = true;
      else this.row.diabetes = false;
      if (this.row.cardiovasculares == "SI") this.row.cardiovasculares = true;
      else this.row.cardiovasculares = false;
      if (this.row.transfuciones == "SI") this.row.transfuciones = true;
      else this.row.transfuciones = false;

      this.enfermedadesAux = [];

      var enfermedades = [];
      enfermedades.push("varicela");
      enfermedades.push("sarampion");
      enfermedades.push("asma");
      enfermedades.push("diabetes");
      enfermedades.push("cardiovasculares");
      enfermedades.push("transfuciones");

      for (let i = 0; i < enfermedades.length; i++) {
        var bandera = enfermedades[i];
        var ayuda = {
          nombre: enfermedades[i],
          estatus: this.row[bandera],
          año: this.fechas[bandera],
        };
        this.enfermedadesAux.push(ayuda);
      }

      console.log(this.enfermedadesAux);
      var aux = {
        Antecedentes: this.enfermedadesAux,
        Alergias: this.alergia,
        DescripcionOtro: this.editedItem.expediente.descripcionOtro,
      };

      const paciente = {
        id: this.editedItem.id,
        nombre: this.editedItem.nombre,
        apellidoPat: this.editedItem.apellidoPat,
        apellidoMat: this.editedItem.apellidoMat,
        telefono: this.editedItem.telefono,
        email: this.editedItem.email,
        nombreUsuario: this.editedItem.nombreUsuario,
        role: "Paciente",
        contrasenia: this.editedItem.contrasenia,
        foto: this.editedItem.foto,
        expediente: aux,
        idFacebook: this.editedItem.idFacebook,
        idGoogle: this.editedItem.idGoogle,
      };
      console.log(aux);

      this.$vloading.show();

      axios
        .put(this.url, paciente)
        .then(() => {
          this.$vloading.hide();
          this.$toast.success(
            "Se actualizaron los datos del paciente correctamente"
          );
          this.obtenerPacientes();
          this.close();
        })
        .catch((e) => {
          console.log(e);
          this.$vloading.hide();
          this.$toast.error("No se pudieron actualizar los datos del paciente");
        });
    },
    actualizarFecha(nombre, año) {
      this.fechas[nombre] = año;
    },
    cambiosRadio(item, estatus) {
      this.row[item] = estatus;

      if (estatus == "NO") {
        if (this.objeto != null) {
          this.objeto.forEach((uwu) => {
            if (uwu.nombre == item) uwu.año = null;
          });
        }
      }

      if (this.row[item] == "SI") this.disabledfechas[item] = false;
      else {
        this.disabledfechas[item] = true;
        this.fechas[item] = null;
      }
    },
    reiniciarValores() {
      this.otraDescripcion = null;
      this.objeto = null;
      this.editedItem = null;
      this.objetoAlergias = null;
      this.enfermedadesAux = [];
      this.alergia = null;
      this.row = {
        varicela: null,
        sarampion: null,
        asma: null,
        diabetes: null,
        cardiovasculares: null,
        transfuciones: null,
      };
      this.fechas = {
        varicela: null,
        sarampion: null,
        asma: null,
        diabetes: null,
        cardiovasculares: null,
        transfuciones: null,
      };
      this.disabledfechas = {
        varicela: true,
        sarampion: true,
        asma: true,
        diabetes: true,
        cardiovasculares: true,
        transfuciones: true,
      };
    },
    obtenerEnfermedad(item) {
      return item.charAt(0).toUpperCase() + item.slice(1);
    },
  },
};
</script>

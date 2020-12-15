<template>
  <v-container class="compact-form subir-formulario">
    <v-card>
      <v-toolbar color="light-blue accent-4" dark>
        <v-spacer></v-spacer>
        <v-toolbar-title>EDITAR PERFIL</v-toolbar-title>

        <v-spacer></v-spacer>
      </v-toolbar>

      <v-tabs
        v-model="tab"
        background-color="grey accent-4"
        centered
        dark
        icons-and-text
      >
        <v-tabs-slider></v-tabs-slider>

        <v-tab href="#configuracion">
          Configuración Cuenta
          <v-icon>mdi-cog</v-icon>
        </v-tab>
        <v-tab href="#datos">
          Datos Personales
          <v-icon>mdi-account</v-icon>
        </v-tab>
      </v-tabs>

      <v-tabs-items v-model="tab">
        <v-tab-item :key="2" :value="'configuracion'">
          <div style="display: inline-flex; width: 100%;">
            <div
              style="width: 30%; height:100%"
              align="center"
              justify="center"
            >
              <v-container>
                <h2 class="ma-4" align="center" justify="center">
                  Imagen de Perfil
                </h2>
                <v-avatar
                  :color="!user.foto ? 'orange' : ''"
                  :size="boton"
                  tile
                >
                  <span
                    class="white--text headline"
                    v-if="!urlimg || imagen === undefined"
                    >{{ iniciales }}</span
                  >
                  <v-img v-if="urlimg && imagen !== undefined" :src="urlimg" />
                </v-avatar>
                <br />
                <v-btn class="ma-4" :width="boton" color="blue" dark>
                  <v-file-input
                    @change="actualizarImagen()"
                    centered
                    truncate-length="18"
                    accept="image/png, image/jpeg"
                    label="Cargar imagen"
                    v-model="imagen"
                  ></v-file-input>
                </v-btn>
              </v-container>
            </div>
            <v-divider vertical></v-divider>
            <div style="width: 70%; height:100%">
              <v-container class="ma-4">
                <h2 align="center" justify="center">Datos Cuenta</h2>

                <v-row>
                  <v-col cols="11">
                    <v-text-field
                      v-model="editedItem.nombreUsuario"
                      label="Nombre de Usuario"
                      :rules="rules.nombreUsuario"
                      required
                      outlined
                    ></v-text-field>
                  </v-col>
                </v-row>
                <v-checkbox
                  @click="mostrarValor()"
                  v-model="auxContrasena"
                  label="Cambiar Contraseña"
                  color="info"
                  hide-details
                  v-if="this.user.contrasenia"
                ></v-checkbox>
                <v-row>
                  <v-col cols="11">
                    <v-text-field
                      :disabled="!auxContrasena"
                      :append-icon="show ? 'mdi-eye' : 'mdi-eye-off'"
                      :type="show ? 'text' : 'password'"
                      @click:append="show = !show"
                      label="Contraseña actual"
                      :rules="rules.contrasenia"
                      v-model="actualContra"
                      required
                      outlined
                      v-if="this.user.contrasenia"
                    ></v-text-field>
                  </v-col>
                  <v-col cols="6">
                    <v-text-field
                      :append-icon="show1 ? 'mdi-eye' : 'mdi-eye-off'"
                      :type="show1 ? 'text' : 'password'"
                      @click:append="show1 = !show1"
                      :disabled="flagContrasenia"
                      label="Nueva contraseña"
                      :rules="rules.contrasenia"
                      v-model="nuevaContra"
                      required
                      outlined
                    ></v-text-field>
                  </v-col>
                  <v-col cols="5">
                    <v-text-field
                      :append-icon="show2 ? 'mdi-eye' : 'mdi-eye-off'"
                      :type="show2 ? 'text' : 'password'"
                      @click:append="show2 = !show2"
                      :disabled="flagContrasenia"
                      label="Repetir Contraseña"
                      :rules="rules.contrasenia"
                      v-model="nuevaContraAux"
                      required
                      outlined
                    ></v-text-field>
                  </v-col>
                </v-row>
              </v-container>
            </div>
          </div>
        </v-tab-item>
        <v-tab-item :key="1" :value="'datos'" class="spacing-playground pa-8">
          <h2 align="center" justify="center">Datos Personales</h2>
          <v-form ref="form">
            <v-row>
              <v-col cols="12">
                <v-text-field
                  v-model="editedItem.nombre"
                  label="Nombre"
                  :rules="rules.nombre"
                  required
                  outlined
                ></v-text-field>
              </v-col>
              <v-col cols="12" sm="6">
                <v-text-field
                  v-model="editedItem.apellidoPat"
                  label="Apellido Paterno"
                  :rules="rules.apellidoPat"
                  required
                  outlined
                ></v-text-field>
              </v-col>
              <v-col cols="12" sm="6">
                <v-text-field
                  v-model="editedItem.apellidoMat"
                  label="Apellido Materno"
                  :rules="rules.apellidoMat"
                  outlined
                ></v-text-field>
              </v-col>
              <v-col cols="12" sm="6">
                <v-text-field
                  v-model="editedItem.email"
                  label="Correo Electronico"
                  :rules="rules.email"
                  outlined
                ></v-text-field>
              </v-col>
              <v-col cols="12" sm="6">
                <v-text-field
                  v-model="editedItem.telefono"
                  label="Numero de telefono"
                  :rules="rules.telefono"
                  required
                  outlined
                ></v-text-field>
              </v-col>
            </v-row>
          </v-form>
        </v-tab-item>
      </v-tabs-items>

      <v-layout align-center justify-center>
        <v-btn
          class="ma-4"
          width="48%"
          color="red"
          dark
          depressed
          @click="restaurarValores()"
        >
          Restaurar Valores
          <v-icon dark right>
            mdi-cancel
          </v-icon>
        </v-btn>
        <v-btn
          class="ma-2"
          width="40%"
          color="primary"
          dark
          depressed
          @click="actualizarValores()"
        >
          Guardar
          <v-icon dark right>
            mdi-checkbox-marked-circle
          </v-icon>
        </v-btn>
      </v-layout>
    </v-card>
  </v-container>
</template>

<script>
import axios from "axios";
import { mapState } from "vuex";
import { environment } from "../env/environment";

export default {
  data: () => ({
    url: environment.url + "Usuario",
    dialog: false,
    tab: null,
    dialogDelete: false,
    pacientes: [],
    imagen: null,
    actualContra: "",
    nuevaContraAux: "",
    nuevaContra: "",
    imagenPerfil: "",
    urlimg: null,
    show: false,
    show1: false,
    show2: false,
    auxContrasena: false,

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
      consultorio: "",
      horaInicio: "",
      horaFin: "",
      idFacebook: "",
      idGoogle: ""
    },
    rules: {
      nombre: [val => (val || "").length > 0 || "Este campo es requerido"],
      apellidoPat: [val => (val || "").length > 0 || "Este campo es requerido"],
      telefono: [val => (val || "").length > 0 || "El telefono es requerido"],
      nombreUsuario: [
        val => (val || "").length > 0 || "Nombre de usuario es requerido"
      ],
      contrasenia: [
        val => (val || "").length > 0 || "La contraseña es requerida"
      ]
    }
  }),

  computed: {
    boton() {
      switch (this.$vuetify.breakpoint.name) {
        case "xs":
          return 100;
        case "sm":
          return 130;
        case "md":
          return 250;
        case "lg":
          return 250;
        case "xl":
          return 250;
        default:
          return 100;
      }
    },
    ...mapState(["user"]),
    flagContrasenia() {
      return this.user.contrasenia != "" && !this.auxContrasena;
    },
    iniciales() {
      return this.user.nombre || this.user.apellidoPat
        ? this.user.nombre.charAt(0).toUpperCase() +
            this.user.apellidoPat.charAt(0).toUpperCase()
        : "";
    }
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
    }
  },
  created() {
    this.initialize();
    console.log(this.$vuetify.breakpoint);
  },
  methods: {
    mostrarValor() {},
    actualizarImagen() {
      if (this.imagen !== undefined) {
        var that = this; //the important bit

        var reader = new FileReader();
        reader.readAsDataURL(this.imagen);
        reader.onloadend = function() {
          that.urlimg = reader.result;
        };
      }
    },
    restaurarValores() {
      this.editedItem.nombre = this.user.nombre;
      this.editedItem.apellidoPat = this.user.apellidoPat;
      this.editedItem.apellidoMat = this.user.apellidoMat;
      this.editedItem.email = this.user.email;
      this.editedItem.telefono = this.user.telefono;
      this.editedItem.nombreUsuario = this.user.nombreUsuario;
      this.editedItem.foto = this.user.foto;
      this.editedItem.contrasenia = this.user.contrasenia;
      this.editedItem.id = this.user.id;
      this.editedItem.role = this.user.role;
      this.editedItem.consultorio = this.user.consultorio;
      this.nuevaContra = "";
      this.nuevaContraAux = "";
      this.actualContra = "";
      this.auxContrasena = false;
      this.urlimg = this.user.foto;
      this.imagen = null;
    },

    actualizarValores() {
      if (
        this.user.contrasenia !== this.actualContra &&
        this.auxContrasena === true
      ) {
        this.$toast.error(
          "La contraseña introducida no coincide con su contraseña actual"
        );
        return;
      } else {
        if (
          this.nuevaContra !== this.nuevaContraAux &&
          this.auxContrasena === true
        ) {
          this.$toast.error(
            "Error: No coinciden los datos de la nueva contraseña"
          );
          return;
        } else {
          if (this.auxContrasena === false && this.user.contrasenia)
            this.editedItem.contrasenia = this.user.contrasenia;
          else this.editedItem.contrasenia = this.nuevaContraAux;

          this.editedItem.id = this.user.id;
          this.editedItem.role = this.user.role;
          this.editedItem.consultorio = this.user.consultorio;
          this.editedItem.horaInicio = this.user.horaInicio;
          this.editedItem.horaFin = this.user.horaFin;
          this.editedItem.idFacebook = this.user.idFacebook;
          this.editedItem.idGoogle = this.user.idGoogle;
          if (this.imagen !== undefined) this.editedItem.foto = this.urlimg;
          else this.editedItem.foto = null;
          this.$vloading.show();
          axios
            .put(this.url, this.editedItem)
            .then(() => {
              this.$toast.success("Datos actualizados correctamente");
              this.$vloading.hide();
              localStorage.removeItem("usuario");
              localStorage.setItem("usuario", JSON.stringify(this.editedItem));
              setTimeout(function() {}, 3000);
              this.$router.go();
            })
            .catch(e => {
              console.log(e);
              this.$vloading.hide();
              this.$toast.error("Hubo un error al actualizar los datos");
            });
        }
      }
    },

    initialize() {
      this.datosUsuario();
    },

    datosUsuario() {
      this.editedItem.nombre = this.user.nombre;
      this.editedItem.apellidoPat = this.user.apellidoPat;
      this.editedItem.apellidoMat = this.user.apellidoMat;
      this.editedItem.email = this.user.email;
      this.editedItem.telefono = this.user.telefono;
      this.editedItem.nombreUsuario = this.user.nombreUsuario;
      this.urlimg = this.user.foto;
    }
  }
};
</script>

<style lang="scss" scoped>
.compact-form {
  transform: scale(0.875);
  transform-origin: center;
}

.container {
  padding: 0px !important;
}

.subir-formulario {
  position: relative;
  top: -30px;
}
</style>

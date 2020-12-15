<template>
  <v-app id="inspire">
    <v-main style="background-color:#00ACE6">
      <v-container class="fill-height">
        <v-row class="fill-height sinpadding">
          <v-col class="sinpadding" cols="12" sm="12" md="5" lg="4" xl="3">
            <v-card class="sinpadding" v-if="!register">
              <v-toolbar color="azulSecundario" dark flat>
                <v-toolbar-title>Bienvenido a DentiSmart</v-toolbar-title>
                <v-icon style="margin-left:5px;">mdi-tooth</v-icon>
              </v-toolbar>
              <v-card-text>
                <v-form ref="login">
                  <v-text-field
                    label="Usuario"
                    name="usuario"
                    @keyup.enter="login()"
                    append-icon="mdi-account"
                    type="text"
                    required
                    v-model="nombreUsuario"
                    :rules="[
                      () => !!nombreUsuario || 'Nombre de usuario es requerido'
                    ]"
                  ></v-text-field>
                  <v-text-field
                    id="password"
                    label="Contraseña"
                    name="contrasenia"
                    @keyup.enter="login()"
                    :append-icon="show ? 'mdi-eye' : 'mdi-eye-off'"
                    :type="show ? 'text' : 'password'"
                    required
                    @click:append="show = !show"
                    v-model="contrasenia"
                    :rules="[
                      () => !!contrasenia || 'La Contraseña es requerida'
                    ]"
                  ></v-text-field>
                </v-form>
              </v-card-text>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn @click="login()" color="primary">Ingresar</v-btn>
                <v-btn color="azulSecundario" dark @click="register = true"
                  >Registrar</v-btn
                >
                <v-spacer></v-spacer>
              </v-card-actions>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn
                  style="text-transform:none !important;"
                  color="white"
                  v-google-login-button="clientId"
                  elevation="3"
                  light
                >
                  <v-icon style="margin-right:10px">$custom</v-icon>
                  Iniciar con google
                </v-btn>
                <v-spacer></v-spacer>
              </v-card-actions>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn
                  style="text-transform:none !important;"
                  v-facebook-login-button="appId"
                  color="#166FE5"
                  elevation="3"
                  dark
                >
                  <v-icon style="margin-right:10px">mdi-facebook</v-icon>
                  Iniciar con facebook
                </v-btn>
                <v-spacer></v-spacer>
              </v-card-actions>
            </v-card>
            <v-card class="sinpadding" v-if="register">
              <v-toolbar color="azulSecundario" dark flat>
                <v-btn icon dark @click="register = !register">
                  <v-icon>mdi-arrow-left</v-icon>
                </v-btn>
                <v-toolbar-title>Registarse en DentiSmart</v-toolbar-title>
              </v-toolbar>
              <v-card-text>
                <v-form
                  ref="registerForm"
                  v-model="validRegistrar"
                  lazy-validation
                >
                  <v-row class="registrar">
                    <v-col cols="12" sm="4" md="4">
                      <v-text-field
                        v-model="nombre"
                        label="Nombre"
                        maxlength="20"
                        required
                        name="nombre"
                        :rules="rules.nombre"
                      ></v-text-field>
                    </v-col>
                    <v-col cols="12" sm="4" md="4">
                      <v-text-field
                        v-model="apellidoPat"
                        label="Apellido Paterno"
                        maxlength="20"
                        required
                        name="apellidoPat"
                        :rules="rules.apellidoPat"
                      ></v-text-field>
                    </v-col>
                    <v-col cols="12" sm="4" md="4">
                      <v-text-field
                        v-model="apellidoMat"
                        label="Apellido Materno"
                        maxlength="20"
                        required
                        name="apellidoMat"
                      ></v-text-field>
                    </v-col>
                    <v-col cols="12" sm="6" md="6">
                      <v-text-field
                        v-model="telefono"
                        label="Numero de telefono"
                        required
                        name="telefono"
                        :rules="rules.telefono"
                      ></v-text-field>
                    </v-col>
                    <v-col cols="12" sm="6" md="6">
                      <v-text-field
                        v-model="email"
                        label="Correo Electronico"
                        required
                        name="email"
                        :rules="rules.email"
                      ></v-text-field>
                    </v-col>
                    <v-col cols="12">
                      <v-text-field
                        v-model="nombreUsuario1"
                        label="Nombre de usuario"
                        required
                        name="nombreUsuario"
                        :rules="rules.nombreUsuario"
                      ></v-text-field>
                    </v-col>
                    <v-col cols="12">
                      <v-text-field
                        v-model="contrasenia1"
                        :append-icon="show1 ? 'mdi-eye' : 'mdi-eye-off'"
                        :type="show1 ? 'text' : 'password'"
                        label="Contraseña"
                        hint="Debe tener mas de 8 caracteres"
                        counter
                        @click:append="show1 = !show1"
                        name="contrasenia"
                        :rules="rules.contrasenia"
                      ></v-text-field>
                    </v-col>
                    <v-col cols="12">
                      <v-text-field
                        block
                        v-model="verify"
                        :append-icon="show1 ? 'mdi-eye' : 'mdi-eye-off'"
                        :type="show1 ? 'text' : 'password'"
                        name="contrasenia1"
                        :rules="rules.verify"
                        label="Confirmar Contraseña"
                        counter
                        @click:append="show1 = !show1"
                      ></v-text-field>
                    </v-col>
                  </v-row>
                </v-form>
              </v-card-text>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="azulSecundario" dark @click="singup()"
                  >Registrar</v-btn
                >
                <v-spacer></v-spacer>
              </v-card-actions>
            </v-card>
          </v-col>
          <v-col
            cols="12"
            sm="12"
            md="7"
            lg="7"
            class="sinpadding hidden-sm-and-down"
          >
            <div class="fill-height sinpadding fondo"></div>
          </v-col>
        </v-row>
      </v-container>
    </v-main>
  </v-app>
</template>

<script>
import axios from "axios";
import { environment } from "../env/environment";
import GoogleLoginButton from "vue-google-login-button-directive";
import FacebookLoginButton from "vue-facebook-login-button-directive";
export default {
  directives: {
    GoogleLoginButton,
    FacebookLoginButton
  },
  data: () => ({
    clientId:
      "923969662364-ha57bif28mrd8ur447mfv1pe6fc7g451.apps.googleusercontent.com",
    appId: "887716112033665",
    photo: "",
    apellidoPaternoFace: "",
    apellidoMaternoFace: "",
    apellidoPaternogoogle: "",
    apellidoMaternoGoogle: "",
    first_name: "",
    nombreUsuario: "",
    contrasenia: "",
    register: false,
    show1: false,
    validRegistrar: true,
    validLogin: true,
    email: "",
    telefono: "",
    contrasenia1: "",
    nombreUsuario1: "",
    apellidoPat: "",
    apellidoMat: "",
    nombre: "",
    verify: "",
    show: false,
    url: environment.url + "usuario",
    rules: {
      nombre: [val => (val || "").length > 0 || "El nombre es requerido"],
      apellidoPat: [
        val => (val || "").length > 0 || "El apellido es requerido"
      ],
      contrasenia: [
        val => (val || "").length > 0 || "La cotraseña es requerida"
      ],
      email: [val => (val || "").length > 0 || "Correo es requerido"],
      telefono: [val => (val || "").length > 0 || "El telefono es requerido"],
      verify: [val => (val || "").length > 0 || "La comprobacin es requerida"],
      nombreUsuario: [
        val => (val || "").length > 0 || "Nombre de usuario es requerido"
      ]
    }
  }),
  props: {
    source: String
  },
  methods: {
    OnFacebookAuthSuccess(idToken, perfil) {
      // Receive the idToken and make your magic with the backend
      if (perfil.name) {
        let separa = perfil.name.split(" ");
        this.first_name = separa[0];
        if (separa.length < 3 && separa.length > 0) {
          this.apellidoPaternoFace = separa[1];
        } else {
          if (separa.length == 3) {
            this.apellidoPaternoFace = separa[1];
            this.apellidoMaternoFace = separa[2];
          } else {
            this.apellidoPaternoFace = separa[separa.length - 2];
            this.apellidoMaternoFace = separa[separa.length - 1];
          }
        }
      }
      this.photo = "";
      this.$vloading.show();
      const usuario = {
        nombre: this.first_name,
        apellidoPat: this.apellidoPaternoFace,
        apellidoMat: this.apellidoMaternoFace,
        nombreUsuario: perfil.email,
        contrasenia: "",
        email: perfil.email,
        role: "Paciente",
        telefono: "",
        foto: this.photo,
        idFacebook: perfil.id
      };
      this.$store
        .dispatch("loginFacebook", usuario)
        .then(() => {
          this.$vloading.hide();
          this.$router.push("inicio");
        })
        .catch(() => {
          this.$vloading.hide();
        });
    },
    OnFacebookAuthFail() {},
    OnGoogleAuthSuccess(idToken, perfil) {
      // Receive the idToken and make your magic with the backend
      if (perfil.getFamilyName()) {
        let separa = perfil.getFamilyName().split(" ");
        this.apellidoPaternogoogle = separa[0];
        if (separa[1] !== undefined) {
          this.apellidoMaternoGoogle = separa[1];
        }
      }
      this.$vloading.show();
      const usuario = {
        nombre: perfil.getGivenName(),
        apellidoPat: this.apellidoPaternogoogle,
        apellidoMat: this.apellidoMaternoGoogle,
        nombreUsuario: perfil.getEmail(),
        contrasenia: "",
        email: perfil.getEmail(),
        role: "Paciente",
        idGoogle: perfil.getId(),
        telefono: "",
        foto: perfil.getImageUrl()
      };
      this.$store
        .dispatch("loginGoogle", usuario)
        .then(() => {
          this.$vloading.hide();
          this.$router.push("inicio");
        })
        .catch(() => {
          this.$vloading.hide();
        });
    },
    OnGoogleAuthFail() {},
    login() {
      if (this.$refs.login.validate()) {
        const nombreUsuario = this.nombreUsuario;
        const contrasenia = this.contrasenia;
        this.$vloading.show();
        this.$store
          .dispatch("login", {
            nombreUsuario,
            contrasenia
          })
          .then(() => {
            this.$vloading.hide();
            this.$router.push("inicio");
          })
          .catch(() => {
            this.$vloading.hide();
            this.$toast.error("Usuario o contraseña son incorrectos");
          });
      }
    },
    singup() {
      if (this.$refs.registerForm.validate()) {
        this.$vloading.show();
        const nuevo = {
          nombre: this.nombre,
          apellidoPat: this.apellidoPat,
          apellidoMat: this.apellidoMat,
          nombreUsuario: this.nombreUsuario1,
          contrasenia: this.contrasenia1,
          email: this.email,
          role: "Paciente",
          telefono: this.telefono
        };
        axios
          .post(this.url, nuevo)
          .then(() => {
            const nombreUsuario = this.nombreUsuario1;
            const contrasenia = this.contrasenia1;
            this.$store
              .dispatch("login", {
                nombreUsuario,
                contrasenia
              })
              .then(() => {
                this.$vloading.hide();
                this.$router.push("inicio");
              })
              .catch(() => {
                this.$vloading.hide();
                this.$toast.error("No se pudo iniciar sesesion");
              });
          })
          .catch(() => {
            this.$vloading.hide();
            this.$toast.error("El nombre de usuario ya existe");
          });
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.fondo {
  background-image: url("../assets/login-background.jpg");
  background-size: cover;
}

.sinpadding {
  padding: 0px;
}

.registrar > * {
  padding-bottom: 1px;
  padding-top: 1px;
}
</style>

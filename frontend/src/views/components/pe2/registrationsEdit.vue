<template>
  <div>
    <vs-row vs-justify="center">
      <vs-col type="flex" vs-justify="center" vs-align="center" :vs-lg="12"
              vs-sm="6" vs-xs="12"
              code-toggler>
        <vs-card class="cardx">
          <div slot="header">
            <h2 class="float-left" style="color: cornflowerblue">Students List </h2>
            <div class="float-right mb-1">
              <vs-button @click="addForm = true" color="primary" icon="add" type="filled">Add New
                Student
              </vs-button>
            </div>
          </div>
          <div class="table-responsive">
            <table class="table v-middle border">
              <thead>
              <tr class="">
                <th class="border-top-0" style="color: cornflowerblue">Name</th>
                <th class="border-top-0" style="color: cornflowerblue">Prename</th>
                <th class="border-top-0" style="color: cornflowerblue">Email</th>
                <th class="border-top-0" style="color: cornflowerblue">ILIAS Name</th>
                <th class="border-top-0" style="color: cornflowerblue">Actions</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="(student,index) in students" :key="student.id">
                <td> {{ student.name }}</td>
                <td> {{ student.prename }}</td>
                <td> {{ student.email }}</td>
                <td> {{ student.iliasName }}</td>
                <td>
                  <div>
                    <vs-button @click="deleteForm = true; currentStudent = student" icon="delete" class="m-1"
                               color="danger"
                               type="filled">
                      Delete
                    </vs-button>
                    <vs-button @click="getStudentData(index);editForm=true" icon="edit" class="m-1" color="warning"
                               type="filled">
                      Edit
                    </vs-button>
                  </div>
                </td>
              </tr>
              </tbody>
            </table>
          </div>
        </vs-card>
      </vs-col>


      <vs-prompt
          title="Add New Student"
          color="success"
          @cancel='resetAllValues();notify("Closed","Add was closed successfully","warning")'
          @accept="addStudent"
          @close='resetAllValues();notify("Closed","Add was closed successfully","warning")'
          :is-valid="validStudent()"
          :active.sync="addForm"
      >
        <div class="con-exemple-prompt">
          <vs-input label-placeholder="Name" v-model="name"/>
          <small class="form-text text-muted mb-4">The name must not be empty</small>
          <vs-input label-placeholder="Prename" v-model="prename"/>
          <small class="form-text text-muted mb-4">The name must not be empty.</small>
          <vs-input label-placeholder="Email" type="mail" v-model="email"/>
          <small class="form-text text-muted mb-4">The Email must and end with
            <i>@stud.uni-stuttgart.de</i></small>
          <vs-input label-placeholder="ILIAS Name" v-model="iliasName"/>
          <small class="form-text text-muted mb-4">The first character must be a Capital letter.</small>
          <vs-alert
              :active="!validStudent()"
              color="danger"
              icon="new_releases"
          >

            Your input does not match the needed one
            or this student exists in another registration
          </vs-alert>
        </div>
      </vs-prompt>

      <vs-prompt
          title="Edit Student"
          color="success"
          @cancel='resetAllValues();notify("Closed","Edit was closed successfully","warning")'
          @accept="saveChanges()"
          @close='resetAllValues();notify("Closed","Edit was closed successfully","warning")'
          :is-valid="validStudentEdit()"
          :active.sync="editForm"
      >
        <div class="con-exemple-prompt">
          <vs-input label="Name" :placeholder="nameEdit" v-model="nameEdit"/>
          <small class="form-text text-muted mb-4">The name must not be empty</small>
          <vs-input label="Prename" :placeholder="prenameEdit" v-model="prenameEdit"/>
          <small class="form-text text-muted mb-4">The name must not be empty.</small>
          <vs-input label="Email" type="mail" :placeholder="emailEdit" v-model="emailEdit"/>
          <small class="form-text text-muted mb-4">The Email must and end with
            <i>@stud.uni-stuttgart.de</i></small>
          <vs-input label="ILIAS Name" :placeholder="iliasEdit" v-model="iliasEdit"/>
          <small class="form-text text-muted mb-4">The first character must be a Capital letter.</small>
          <vs-alert
              :active="!validStudentEdit()"
              color="danger"
              icon="new_releases"
          >

            Your input does not match the needed one
            or this student exists in another registration
          </vs-alert>
        </div>
      </vs-prompt>


    </vs-row>

    <vs-row vs-justify="center">
      <vs-col type="flex" vs-justify="center" vs-align="center" :vs-lg="8"
              vs-sm="6" vs-xs="12"
              code-toggler>
        <vs-card class="cardx">
          <div slot="header">
            <h2 class="float-left" style="color: cornflowerblue">Projects List </h2>
          </div>
          <div class="table-responsive">
            <table class="table v-middle border">
              <thead>
              <tr class="">
                <th class="border-top-0" style="color: cornflowerblue">ID</th>
                <th class="border-top-0" style="color: cornflowerblue">Name</th>
                <th class="border-top-0" style="color: cornflowerblue">Actions</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="project in projects" :key="project.id">
                <td> {{ project.id }}</td>
                <td> {{ project.name }}</td>
                <td>
                  <div>
                    <vs-button v-bind:disabled="projectChosen(project.id)"
                               @click="addToPreferedProjects(project.id,project.name);" icon="add" class="m-1"
                               color="success" type="filled">
                      Add
                    </vs-button>
                    <vs-button v-bind:disabled="!projectChosen(project.id)"
                               @click="removeFromPreferedProjects(project.id,project.name);" icon="delete" class="m-1"
                               color="danger" type="filled">
                      Remove
                    </vs-button>
                  </div>
                </td>
              </tr>
              </tbody>
            </table>
          </div>
        </vs-card>
      </vs-col>
      <vs-col type="flex" vs-justify="center" vs-align="center" :vs-lg="4"
              vs-sm="6" vs-xs="12"
              code-toggler>
        <vs-card class="cardx">
          <div slot="header">
            <h2 class="float-left" style="color: cornflowerblue">Submission </h2>
          </div>
          <div class="table-responsive">
            <h4 class="mb-3">You have chosen your preferred projects in the next way</h4>
            <ol style="  list-style-type: none;" class="ml-4" v-for="(project, index)  in preferedProjectNames"
                :key="project.index">
              <li style="font-size: 16px;font-weight: bold"> {{ index + 1 }}. Favourite Project : {{ project }}</li>
            </ol>
          </div>

          <div style="height: 40px">
            <div @click="submitRegistration();resetValuesAfterSubmission()" class="m-3">
              <vs-button color="success" type="relief" icon="cloud_upload"
                         v-bind:disabled="!validSubmission()">Submit Registration
              </vs-button>
            </div>
          </div>
        </vs-card>
      </vs-col>


      <vs-prompt
          title="Add New Project"
          color="success"
          @cancel='resetAllValues();notify("Closed","Add was closed successfully","warning")'
          @accept="addStudent"
          @close='resetAllValues();notify("Closed","Add was closed successfully","warning")'
          :is-valid="validStudent()"
          :active.sync="addForm"
      >
        <div class="con-exemple-prompt">
          <vs-input label-placeholder="Name" v-model="name"/>
          <small class="form-text text-muted mb-4">The name must not be empty</small>
          <vs-input label-placeholder="Prename" v-model="prename"/>
          <small class="form-text text-muted mb-4">The name must not be empty.</small>
          <vs-input label-placeholder="Email" type="mail" v-model="email"/>
          <small class="form-text text-muted mb-4">The Email must start with st and end with
            <i>@stud.uni-stuttgart.de</i>.</small>
          <vs-input label-placeholder="ILIAS Name" v-model="iliasName"/>
          <small class="form-text text-muted mb-4">The first character must be a Capital letter.</small>
          <vs-alert
              :active="!validStudent()"
              color="danger"
              icon="new_releases"
          >
            Your input does not match the needed one
          </vs-alert>
        </div>
      </vs-prompt>

      <vs-prompt
          title="Edit Student"
          color="success"
          @cancel='resetAllValues();notify("Closed","Edit was closed successfully","warning")'
          @accept="saveChanges()"
          @close='resetAllValues();notify("Closed","Edit was closed successfully","warning")'
          :is-valid="validStudentEdit()"
          :active.sync="editForm"
      >
        <div class="con-exemple-prompt">
          <vs-input label="Name" :placeholder="nameEdit" v-model="nameEdit"/>
          <small class="form-text text-muted mb-4">The name must not be empty</small>
          <vs-input label="Prename" :placeholder="prenameEdit" v-model="prenameEdit"/>
          <small class="form-text text-muted mb-4">The name must not be empty.</small>
          <vs-input label="Email" type="mail" :placeholder="emailEdit" v-model="emailEdit"/>
          <small class="form-text text-muted mb-4">The Email must start with st and end with
            <i>@stud.uni-stuttgart.de</i>.</small>
          <vs-input label="ILIAS Name" :placeholder="iliasEdit" v-model="iliasEdit"/>
          <small class="form-text text-muted mb-4">The first character must be a Capital letter.</small>
          <vs-alert
              :active="!validStudentEdit()"
              color="danger"
              icon="new_releases"
          >
            Your input does not match the needed one
          </vs-alert>
        </div>
      </vs-prompt>
      <vs-prompt
          title="Deletion"
          color="red"
          @close='notify("Closed","Deletion was cancelled.","warning")'
          @cancel='notify("Closed","Deletion was cancelled.","warning")'
          @accept="deleteStudent(currentStudent)"
          :is-valid="true"
          :active.sync="deleteForm"
      >
        <div class="con-exemple-prompt">
          Are you sure you want to delete <br>
          <h6>{{ currentStudent.prename }}</h6>
        </div>
      </vs-prompt>


    </vs-row>


  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: "registrationsEdit",
  data() {
    return {
      registration: {},
      registrationsStudents: [],
      currentStudent: {},
      name: '',
      prename: '',
      email: '',
      iliasName: '',

      //for the edit
      nameEdit: '',
      prenameEdit: '',
      emailEdit: '',
      iliasEdit: '',
      indexStudentEdit: 0,

      students: [],
      projects: [{"id": 1, "name": "Project 1"}],
      preferedProjectIDs: [],
      preferedProjectNames: [],
      addForm: false,
      editForm: false,
      deleteForm: false
    }
  },


  created: async function () {
    this.registration = this.$route.params.registration;
    await this.fetchAllProjects();
    this.preferedProjectIDs = this.registration.preferredProjectIds;
    this.students = this.registration.students
    await this.fetchAllRegistrations()
    this.getProjectNames();

  },


  methods: {
    fetchAllRegistrations: async function () {
      await axios.get(`http://localhost:8080/api/v1/submittedRegistrations`)
          .then(response => {
            this.registrationsStudents = response.data.map(registration => registration.students)
          })
          .catch((error) => {
            if (error.response) {
              this.notify("Projects Database Error", error.message, "danger")
            } else {
              this.notify("Projects Database Error", "Connection to Database Error", "danger")
            }
          })
    },
    getProjectNames: function () {

      for (let i = 0; i < this.preferedProjectIDs.length; i++) {
        for (let j = 0; j < this.projects.length; j++) {

          if (parseInt(this.projects[j].id) !== parseInt(this.preferedProjectIDs[i])) {
            if (!this.preferedProjectNames.includes(this.projects[j].name)) {
              this.preferedProjectNames.push(this.projects[j].name)
            }
          }
        }
      }
    },
    projectChosen(id) {
      return this.preferedProjectIDs.includes(id)
    },
    addToPreferedProjects: function (id, name) {
      if (!this.preferedProjectIDs.includes(id)) {
        this.preferedProjectIDs.push(id);
        this.preferedProjectNames.push(name)
      }
    },
    validSubmission() {
      return (this.students.length !== 0 && this.preferedProjectIDs.length === this.projects.length)
    },
    removeFromPreferedProjects: function (id, name) {
      for (let i = 0; i < this.preferedProjectIDs.length; i++) {
        if (this.preferedProjectIDs[i] === id) {
          this.preferedProjectIDs.splice(i, 1)
        }
      }
      for (let i = 0; i < this.preferedProjectNames.length; i++) {
        if (this.preferedProjectNames[i] === name) {
          this.preferedProjectNames.splice(i, 1)
        }
      }

    }, resetValuesAfterSubmission: function () {
      this.students = []
      this.preferedProjectIDs = []
      this.preferedProjectNames = []
    },

    submitRegistration: async function () {

      await axios.put(`http://localhost:8080/api/v1/registrations/` + this.registration.id,
          {"students": this.students, "preferredProjectIds": this.preferedProjectIDs}).then(() => {

        this.notify("Confirmation", "Submission successfully performed", "success")
        this.$router.push({name: "Submitted Registration", params: {}});

      },)
          .catch(function (error) {

                if (error.response) {
                  if (error.response.status === 412) {
                    this.notify("Danger", "Submission not performed", "danger")

                  }
                }
              }
          );

    },

    saveChanges: function () {


      this.students[this.indexStudentEdit].prename = this.prenameEdit;
      this.students[this.indexStudentEdit].name = this.nameEdit;
      this.students[this.indexStudentEdit].iliasName = this.iliasEdit;
      this.students[this.indexStudentEdit].email = this.emailEdit;
    },
    getStudentData: function (id) {
      this.currentStudent = this.students[id]
      this.prenameEdit = this.students[id].prename;
      this.nameEdit = this.students[id].name;
      this.emailEdit = this.students[id].email;
      this.iliasEdit = this.students[id].iliasName;
      this.indexStudentEdit = id;


    },
    deleteStudent: function (student) {
      for (let i = 0; i < this.students.length; i++) {
        if (this.students[i].email === student.email) this.students.splice(i, 1)
        this.notify("Confirmation", 'Deletion was successful', "danger")
      }


    },
    validStudent() {
      let studentAlreadyExists = false;
      for (let i = 0; i < this.students.length; i++) {
        if (this.students[i].iliasName === this.iliasName || this.students[i].email === this.email) {
          studentAlreadyExists = true;
          break;
        }
      }
      for (let j = 0; j < this.registrationsStudents.length; j++) {
        for (let i = 0; i < this.registrationsStudents[j].length; i++) {
          if (this.registrationsStudents[j][i].email === this.email || this.registrationsStudents[j][i].iliasName === this.iliasName) {

            studentAlreadyExists = true;
            break;
          }
        }
      }
      return (!studentAlreadyExists && this.email.endsWith("@stud.uni-stuttgart.de") && !this.email.startsWith("@") && this.prename.length !== 0
          && this.name.length !== 0 && this.iliasName.length !== 0 && this.iliasName.charAt(0) === this.iliasName.charAt(0).toUpperCase())
    },

    validStudentEdit() {
      let studentAlreadyExists = false;
      for (let i = 0; i < this.students.length; i++) {
        if (this.students[i].iliasName === this.iliasEdit || this.students[i].email === this.emailEdit) {
          studentAlreadyExists = true;
          break;
        }
      }
      for (let j = 0; j < this.registrationsStudents.length; j++) {
        for (let i = 0; i < this.registrationsStudents[j].length; i++) {
          if (this.registrationsStudents[j][i].email === this.emailEdit || this.registrationsStudents[j][i].iliasName === this.iliasEdit) {
            studentAlreadyExists = true;
            break;
          }
        }
      }
      if (this.currentStudent.email === this.emailEdit || this.currentStudent.iliasEdit === this.iliasEdit) studentAlreadyExists = false

      return (!studentAlreadyExists && this.emailEdit.endsWith("@stud.uni-stuttgart.de") && !this.emailEdit.startsWith("@") && this.prenameEdit.length !== 0
          && this.nameEdit.length !== 0 && this.iliasEdit.length !== 0 && this.iliasEdit.charAt(0) === this.iliasEdit.charAt(0).toUpperCase())
    },
    addStudent: function () {

      this.students = this.students.concat({
        "prename": this.prename,
        "name": this.name,
        "email": this.email,
        "iliasName": this.iliasName,
      })
      this.name = '';
      this.prename = '';
      this.email = '';
      this.iliasName = '';
      this.addForm = false;
    },
    /** Resets all values of input and edit fields. Also resets the values for the employee dropdown*/
    resetAllValues: function () {
      this.prename = '';
      this.name = '';
      this.email = '';
      this.iliasName = '';
      this.iliasEdit = '';
      this.nameEdit = '';
      this.prenameEdit = '';
      this.emailEdit = '';
    },


    /**
     * Gets all projects from DB
     */
    fetchAllProjects: async function () {
      await axios.get(`http://localhost:8080/api/v1/projects`)
          .then(response => {
            // JSON responses are automatically parsed.
            this.projects = response.data
          })
          .catch((error) => {
            if (error.response) {
              this.notify("Projects Database Error", error.message, "danger")
            } else {
              this.notify("Projects Database Error", "Connection to Database Error", "danger")
            }
          })
    },


    /** Shows prompt with title, message and selected color*/
    notify: function (title, message, color) {
      this.$vs.notify({
        title: title,
        text: message,
        color: color, type: "gradient",
      })
    },


  }


}
</script>

<style>

</style>
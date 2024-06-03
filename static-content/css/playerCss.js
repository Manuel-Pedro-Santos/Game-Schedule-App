export function loginCss() {
    return `
    .login-form {
     background-color: #ffffff;
      display: flex;
      flex-direction: column;
      width: 300px;
      margin: 50px auto;
      padding: 20px;
      border: 1px solid #ddd;
      border-radius: 5px;
    }

    .login-form input {
      padding: 10px;
      margin-bottom: 10px;
      border: 1px solid #ccc;
      border-radius: 3px;
    }

    .login-form input[type="submit"] {
      background-color: #000080;
      color: white;
      border: none;
      cursor: pointer;
    }
  `
}

export function signUpCss() {
    return `
      .signup-form {
     background-color: #ffffff;
      display: flex;
      flex-direction: column;
      width: 300px;
      margin: 50px auto;
      padding: 20px;
      border: 1px solid #ddd;
      border-radius: 5px;
    }

    .signup-form input {
      padding: 10px;
      margin-bottom: 10px;
      border: 1px solid #ccc;
      border-radius: 3px;
    }
    `
}
# TKXDPM.VN.20231-01
Template for managing Capstone's project in the Software Design and Construction course in the 2023.1 semester.

## Table of contents

- [TKXDPM.VN.20231-01](#tkxdpmvn20231-01)
  - [Table of contents](#table-of-contents)
  - [Quick start](#quick-start)
  - [What's included](#whats-included)
  - [Report Content](#report-content)
  - [Pull request template](#pull-request-template)

## Quick start

Before using this Github repository, everyone needs to register their group information using the table below:

| Name                 | Role        |
| :------------------- | :---------- |
| Do Dang Phuong       | Team Leader |
| Nguyen Manh Phuong   | Member      |
| Bui Trung Quan       | Member      |
| Lam Anh Quan         | Member      |
## What's included

The recommended structure is as follows:

- `AIMS`: folder containing the team's AIMS base code
- `assets`: folder containing images that you want to include in the report file
- `README.md`: weekly report file, individuals are required to update their tasks and how their do it into this file
- `Template.md`: template for the weekly report
- `pull_request_template.md`: pull request's description template
- `how_to_run_AMIS.md`: tutorials to run AMIS

## Report Content

The recommended report structure will have a format as follows:

<details>
  <summary>W1: 27/11/2023~3/12/2023</summary>
<br>
<details>
<summary>Do Dang Phuong</summary>
<br>

- Assigned tasks: Evaluate the coupling of methods in BaseController, HomeController & ViewCartController

- Implementation details:
  - Pull Request(s): https://github.com/PhuongDo1801/TKXDPM.KHMT.20231-18/pull/3

  
</details>

<details>
<summary>Lam Anh Quan</summary>
<br>

- Assigned tasks: Evaluate the coupling of methods in PlaceOrderController

- Implementation details:
  - Pull Request(s): https://github.com/PhuongDo1801/TKXDPM.KHMT.20231-18/pull/1


</details>

<details>
<summary>Nguyen Manh Phuong</summary>
<br>

- Assigned tasks: Evaluate the coupling of methods in PaymentController

- Implementation details:
  - Pull Request(s): https://github.com/PhuongDo1801/TKXDPM.KHMT.20231-18/pull/2

</details>

</details>

<details>

  <summary>W2: 04/12/2023~10/12/2023 </summary>
<br>
<details>
<summary>Do Dang Phuong</summary>
<br>

- Assigned tasks: cohesion analysis in BaseController and HomeController

- Implementation details:
  - Pull Request(s): https://github.com/PhuongDo1801/TKXDPM.KHMT.20231-18/pull/7

</details>

<details>
<summary>Lam Anh Quan</summary>
<br>

- Assigned tasks: cohesion analysis in PlaceOrderController

- Implementation details:
  - Pull Request(s): https://github.com/PhuongDo1801/TKXDPM.KHMT.20231-18/pull/11

</details>

<br>
<details>
<summary>Nguyen Manh Phuong</summary>
<br>

- Assigned tasks: cohesion analysis in  class PaymentController

- Implementation details:
  - Pull Request(s): https://github.com/PhuongDo1801/TKXDPM.KHMT.20231-18/pull/9

</details>

details>
<summary>Bui Trung Quan</summary>
<br>

- Assigned tasks: cohesion analysis in  class ViewCartController

- Implementation details:
  - Pull Request(s): https://github.com/PhuongDo1801/TKXDPM.KHMT.20231-18/pull/12

</details>

</details>

<details>
  <summary>W3: 11/12/2023~17/12/2023</summary>
<br>
<details>
<summary>Nguyen Manh Phuong</summary>
<br>
- Assigned tasks: Evaluate the solid in PaymentController, entity/cart, views/screen/cart, views/screen/home
  
- Implementation details:
  - Pull Request(s): https://github.com/PhuongDo1801/TKXDPM.KHMT.20231-18/pull/14
</details>
<details>
<summary>Do Dang Phuong</summary>
<br>
- Assigned tasks: Evaluate the solid in BaseController va HomeController, entity/cart, views/screen/BaseScreenHandler, views/screen/popup/PopupScreen
  
- Implementation details:
  - Pull Request(s): 
</details>
</details>

---

## Pull request template

- You are required to create a pull request according to these steps:
  - Use the `pull_request_template.md` file when writing the description section in the pull request
  - The `title` of the pull request should follow the format below:
    - `Feature/Topic/Hotfix/Fix: Task Name`. Example: `Feature: Build View Controller`
    - Explain:
      - `Feature`: When the branch has the task of changing the main code of the project
      - `Topic`: When the branch only has the task of research, without directly changing the main code of the project
      - `Hotfix`: When you discover code on the production environment
      - `Fix`: When you discover a bug in a branch that has not been merged into the production environment
  - After creating the pull request, decide who will merge the code within your team.
  - You don't need to follow the Wxx format as I mentioned before
  - Each person will have multiple pull requests
  - Whoever makes the pull request, attach that pull request to the report you attached above. Section `pull request(s)`

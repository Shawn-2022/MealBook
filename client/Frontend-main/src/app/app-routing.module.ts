import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AboutUsComponent } from './about-us/about-us.component';
import { HomePageComponent } from './home-page/home-page.component';
import { TermsConditionComponent } from './terms-condition/terms-condition.component';
import { ChangePasswordComponent } from './change-password/change-password.component';
import { PrivacyPolicyComponent } from './privacy-policy/privacy-policy.component';
import { LoginComponent } from './login/login.component';
const routes: Routes = [ 
  { path: 'home', component: HomePageComponent },
{ path:'about-us',component:AboutUsComponent},
{path:'terms-condition',component:TermsConditionComponent},
{path:'privacy-policy',component:PrivacyPolicyComponent},
{path:'change-password',component:ChangePasswordComponent},
{path:'',component:LoginComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

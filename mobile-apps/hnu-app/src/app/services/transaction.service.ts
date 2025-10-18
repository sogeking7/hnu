import { inject, Injectable } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { TransactionModel, TransactionResourceService } from '@hnu-app/nu-api';

@Injectable({
  providedIn: 'root'
})
export class TransactionService {
  private readonly transaction = inject(TransactionResourceService);

  getUserTransactions(userId: string): Promise<TransactionModel[]> {
    return firstValueFrom(this.transaction.getUserTransactions(userId));
  }

}

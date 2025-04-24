import Image from 'next/image'

import { Button } from '@/components/Button'
import { Heading } from '@/components/Heading'
import logoGo from '@/images/logos/go.svg'
import logoNode from '@/images/logos/node.svg'
import logoPhp from '@/images/logos/php.svg'
import logoPython from '@/images/logos/python.svg'
import logoRuby from '@/images/logos/ruby.svg'
import logoJava from '@/images/logos/java.svg'
import logoTS from '@/images/logos/ts.svg'

const libraries = [
	{
		href: '#java',
		name: 'Java',
		description:
			'One of the most used languages for business applications. This SDK allows to access Quti from your Java application',
		logo: logoJava,
	},
	{
		href: '#type-script-java-script',
		name: 'Typescript/JavaScript',
		description:
			'The languages of the web. This SDK allows you to directly interface with Quti from a your web application',
		logo: logoTS,
	},
]

export function Libraries() {
	return (
		<div className="my-16 xl:max-w-none">
			<Heading level={2} id="official-libraries">
				Official libraries
			</Heading>
			<div className="not-prose mt-4 grid grid-cols-1 gap-x-6 gap-y-10 border-t border-zinc-900/5 pt-10 sm:grid-cols-2 xl:max-w-none xl:grid-cols-3 dark:border-white/5">
				{libraries.map((library) => (
					<div key={library.name} className="flex flex-row-reverse gap-6">
						<div className="flex-auto">
							<h3 className="text-sm font-semibold text-zinc-900 dark:text-white">
								{library.name}
							</h3>
							<p className="mt-1 text-sm text-zinc-600 dark:text-zinc-400">
								{library.description}
							</p>
							<p className="mt-4">
								<Button href={library.href} variant="text" arrow="right">
									Read more
								</Button>
							</p>
						</div>
						<Image
							src={library.logo}
							alt=""
							className="h-12 w-12"
							unoptimized
						/>
					</div>
				))}
			</div>
		</div>
	)
}
